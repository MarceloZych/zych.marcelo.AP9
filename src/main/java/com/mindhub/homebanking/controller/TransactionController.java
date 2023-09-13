package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication authentication
    ){
        Client client =  clientService.findClientByEmailService(authentication.getName());
        Account accountFrom = accountService.findAccountNumberService(fromAccountNumber);
        Account accountDestiny = accountService.findAccountNumberService(toAccountNumber);

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty())  {
            return new ResponseEntity<>("Missing account information", HttpStatus.FORBIDDEN);
        }
        if (amount == 0){
            return new ResponseEntity<>("Amount can't be equal to cero", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()){
            return new ResponseEntity<>("Description is needed", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.equals(accountDestiny)){
            return new ResponseEntity<>("You cannot send to the same account number", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existByNumberService(fromAccountNumber)){
            return new ResponseEntity<>("Origin account is not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountFrom)){
            return new ResponseEntity<>("Account dosen't belong to you", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existByNumberService(toAccountNumber)){
            return new ResponseEntity<>("Account not exist", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance() < amount){
            return new ResponseEntity<>("Amount is not enough", HttpStatus.FORBIDDEN);
        }

        Transaction newTransactionDebit = new Transaction(-amount, description, LocalDateTime.now(), TransactionType.DEBIT);
        transactionService.transactionSaveService(newTransactionDebit);
        Transaction newTransactionCredit = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);
        transactionService.transactionSaveService(newTransactionCredit);

        accountFrom.addTransactions(newTransactionDebit);
        accountDestiny.addTransactions(newTransactionCredit);

        double balanceAccountFrom = accountFrom.getBalance();
        double balanceAccountDestiny = accountDestiny.getBalance();

        accountFrom.setBalance(balanceAccountFrom - amount);
        accountDestiny.setBalance(balanceAccountDestiny + amount);

        accountService.saveAccountService(accountFrom);
        accountService.saveAccountService(accountDestiny);

        return new ResponseEntity<>("Transaction succefull !", HttpStatus.CREATED);
    }
}
