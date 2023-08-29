package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.AccountRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;

    // relacion muchos a uno
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientAccount_id")
    private Client clientAccount;
    @OneToMany(mappedBy = "accountTransaction", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {

    }

    public Account(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(Client clientAccount) {
        this.clientAccount = clientAccount;
    }

    public Set<Transaction> getTransactions(){
        return transactions;
    }
    public void addTransactionSet(Transaction transaction){
        transaction.setAccountTransaction(this);
        transactions.add(transaction);
    }

    public static String generateAccountNumber(AccountRepository accountRepository){
        long number;
        String numberAccount = "";

        do{
            number = generateRandomNumber(10000000, 99999999);
            numberAccount = "VIN-" + number;
        }while (accountRepository.existsByNumber(numberAccount));

        return numberAccount;
    }
    public static long generateRandomNumber(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }
}
