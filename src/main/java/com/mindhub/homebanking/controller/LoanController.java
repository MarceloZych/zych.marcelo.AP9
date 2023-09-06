package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public Set<LoanDTO> getAllLoans () {
        List<Loan> allLoans = loanRepository.findAll();

        Set<LoanDTO> loanDTOS = allLoans
                .stream()
                .map(loans -> new LoanDTO(loans))
                .collect(Collectors.toSet());

        return loanDTOS;
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication){
        // mothod to get
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Loan loan = loanRepository.findLoanById(loanApplicationDTO.getLoanId());

        // client loan verification
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

        if (!accountRepository.existsByNumberAndClient(account.getNumber(), client)) {
            return new ResponseEntity<>("The account is wrong", HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.existsById(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("The loan is not valid", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The loan is not aviable", HttpStatus.FORBIDDEN);
        }

        if (account == null){
            return new ResponseEntity<>("The account don't exist", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments());

        client.addClientLoan(clientLoan);
        loan.addClientLoans(clientLoan);
        clientLoanRepository.save(clientLoan);

        // actualizar saldo
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), loan.getName()+" transaction approved", LocalDateTime.now(), TransactionType.DEBIT);

        account.addTransactions(transaction);
        transactionRepository.save(transaction);

        double accountBalance = account.getBalance();
        account.setBalance(accountBalance + loanApplicationDTO.getAmount());
        accountRepository.save(account);

        return new ResponseEntity<>("Transaction succefull", HttpStatus.ACCEPTED);
    }

}
