package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

    @Entity
    public class Client {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private long id;
        private String firstName, lastName, email, password;

        @OneToMany(mappedBy = "clientAccount", fetch = FetchType.EAGER)
        private Set<Account> accounts = new HashSet<>();
        @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
        private Set<ClientLoan> clientLoans = new HashSet<>();
        @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
        private Set<Card> clientCards = new HashSet<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // setter & getter

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // account

    public Set<Account> getAccountSet() {
        return accounts;
    }

    public void addAccountSet(Account account) {
        account.setClientAccount(this);
        accounts.add(account);
    }
    // ClientLoan

    public Set<ClientLoan> getClientLoans(){ return clientLoans;}
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoans.add(clientLoan);
        clientLoan.setClient(this);
    }
    // Card

    public Set<Card> getClientCards(){ return clientCards; }

    public void addCard(Card card){
        clientCards.add(card);
        card.setClient(this);
    }
}
