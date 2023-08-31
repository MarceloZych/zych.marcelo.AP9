package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication authentication
    ){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account accountFrom = accountRepository.findByNumber(fromAccountNumber);
        Account accountDestiny = accountRepository.findByNumber(toAccountNumber);

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
        if (!accountRepository.existsByNumber(fromAccountNumber)){
            return new ResponseEntity<>("Origin account is not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountFrom)){
            return new ResponseEntity<>("Account dosen't belong to you", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(toAccountNumber)){
            return new ResponseEntity<>("Account not exist", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance() < amount){
            return new ResponseEntity<>("Amount is not enough", HttpStatus.FORBIDDEN);
        }

        Transaction newTransactionDebit = new Transaction(-amount, description, LocalDate.now(), TransactionType.DEBIT);
        transactionRepository.save(newTransactionDebit);
        Transaction newTransactionCredit = new Transaction(amount, description, LocalDate.now(), TransactionType.CREDIT);
        transactionRepository.save(newTransactionCredit);

        accountFrom.addTransactions(newTransactionDebit);
        accountDestiny.addTransactions(newTransactionCredit);

        double balanceAccountFrom = accountFrom.getBalance();
        double balanceAccountDestiny = accountDestiny.getBalance();

        accountFrom.setBalance(balanceAccountFrom - amount);
        accountDestiny.setBalance(balanceAccountDestiny + amount);

        accountRepository.save(accountFrom);
        accountRepository.save(accountDestiny);

        return new ResponseEntity<>("Transaction succefull", HttpStatus.CREATED);
    }
}
