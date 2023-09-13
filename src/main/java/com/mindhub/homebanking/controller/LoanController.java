package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;


//    @Autowired
//    private ClientLoanRepository clientLoanRepository;
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getAllLoans () {

        return loanService.getLoansService();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication){
        // mothod to get
        Client client = clientService.findClientByEmailService(authentication.getName());
        Account account = accountService.findAccountNumberService(loanApplicationDTO.getToAccountNumber());
        Loan loan = loanService.findIdService(loanApplicationDTO.getLoanId());

        // client
        if ( loanApplicationDTO.getLoanId() == 0 ){
            return new ResponseEntity<>("You need to select a type of loan", HttpStatus.FORBIDDEN);
        }

        if ( loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("You need to select the payments type", HttpStatus.FORBIDDEN);
        }

        if ( loanApplicationDTO.getToAccountNumber().isBlank()){
            return new ResponseEntity<>("The Account is not valid", HttpStatus.FORBIDDEN);
        }

        if ( loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Amount is not valid", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Invalid payments", HttpStatus.FORBIDDEN);
        }

        //Account

        if (account == null){
            return new ResponseEntity<>("The account don't exist", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsByNumberAndClientService(account.getNumber(), client)) {
            return new ResponseEntity<>("The account is wrong", HttpStatus.FORBIDDEN);
        }

        //loan

        if(!loanService.existLoanIdService(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("The loan is not valid", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The loan is not aviable", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments());

        client.addClientLoan(clientLoan);
        loan.addClientLoans(clientLoan);
        clientLoanService.saveClientLoanService(clientLoan);

        // actualizar saldo
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), loan.getName()+" transaction approved", LocalDateTime.now(), TransactionType.DEBIT);

        account.addTransactions(transaction);
        transactionService.transactionSaveService(transaction);

        double accountBalance = account.getBalance();
        account.setBalance(accountBalance + loanApplicationDTO.getAmount());
        accountService.saveAccountService(account);

        return new ResponseEntity<>("Transaction succefull", HttpStatus.ACCEPTED);
    }

}
