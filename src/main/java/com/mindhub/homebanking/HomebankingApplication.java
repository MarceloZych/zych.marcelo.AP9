package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository
	){
		return (args) -> {
			// client
			Client client = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123") );

			clientRepository.save(client);

			// client accounts
			Account account = new Account("VIN001", LocalDate.now(), 5000);
			Account account1 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			client.addAccountSet(account);
			client.addAccountSet(account1);
			accountRepository.save(account);
			accountRepository.save(account1);

			// client Card
			Card card1 = new Card(
					CardType.DEBIT,
					CardColor.GOLD,
					"1224-2587-6589-0040",
					300,
					LocalDate.now(),
					LocalDate.now().plusYears(5),
					"Melba Morel"
			);
			Card card2 = new Card(
					CardType.CREDIT,
					CardColor.TITANIUM,
					"1224-2587-3265-5987",
					200,
					LocalDate.now(),
					LocalDate.now().plusYears(5),
					client.getLastName()+" "+client.getLastName()
			);
			client.addCard(card1);
			client.addCard(card2);
			cardRepository.save(card1);
			cardRepository.save(card2);

			// client2 transactions
			Transaction transaction = new Transaction( 100, "SuperMarket shopping", LocalDate.now(), TransactionType.DEBIT);
			Transaction transaction1 = new Transaction( 200, "SuperMarket shopping", LocalDate.now(), TransactionType.CREDIT);
			account.addTransactions(transaction);
			account1.addTransactions(transaction1);
			transactionRepository.save(transaction);
			transactionRepository.save(transaction1);


			// client 1
			Client client1 = new Client("Marcelo", "Zych", "mz@mindhub.com", passwordEncoder.encode("123"));
			clientRepository.save(client1);
			// client1 accounts
			Account account2 = new Account("VIN003", LocalDate.now(), 15000);
			Account account3 = new Account("VIN004", LocalDate.now().plusDays(1), 17500);
			client1.addAccountSet(account2);
			client1.addAccountSet(account3);
			accountRepository.save(account2);
			accountRepository.save(account3);
			//client 1 card
			Card card3 = new Card(
					CardType.CREDIT,
					CardColor.SILVER,
					"2314-8974-6589-6589",
					200,
					LocalDate.now(),
					LocalDate.now().plusYears(5),
					"Marcelo Zych"
			);
			client1.addCard(card3);
			cardRepository.save(card3);

			// client2 transactions
			Transaction transaction2 = new Transaction( 10, "Clothes shopping", LocalDate.now(), TransactionType.DEBIT);
			Transaction transaction3 = new Transaction( 50, "Tecnology shopping", LocalDate.now(), TransactionType.CREDIT);
			account2.addTransactions(transaction2);
			account3.addTransactions(transaction3);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			// client 2
			Client client3 = new Client("Maria", "Sanches", "mary@mindhub.com", passwordEncoder.encode("123"));
			clientRepository.save(client3);
			// client2 accounts
			Account account4 = new Account("VIN005", LocalDate.now(), 2500);
			Account account5 = new Account("VIN006", LocalDate.now(), 3000);
			client3.addAccountSet(account4);
			client3.addAccountSet(account5);
			accountRepository.save(account4);
			accountRepository.save(account5);
			// client2 transaction
			Transaction transaction4 = new Transaction( 100, "Tecnology Shopping", LocalDate.now(), TransactionType.CREDIT);
			Transaction transaction5 = new Transaction( 50, "SuperMarket Shopping", LocalDate.now(), TransactionType.DEBIT);
			account4.addTransactions(transaction4);
			account5.addTransactions(transaction5);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);

			// payments
			Loan mortgage = new Loan("Hipotecario", 500000, new HashSet<>(Arrays.asList(12, 24, 36, 48, 60)));
			Loan personal = new Loan("Personal", 100000, new HashSet<>(Arrays.asList(6, 12, 24)));
			Loan automobile = new Loan("Automotriz", 300000, new HashSet<>(Arrays.asList(6, 12, 24, 36)));
			loanRepository.saveAll(Arrays.asList(mortgage, personal, automobile));

			// client payment
			ClientLoan clientLoan1 = new ClientLoan(400000,60, client, mortgage);
			ClientLoan clientLoan2 = new ClientLoan(50000,12, client, personal);
			ClientLoan clientLoan3 = new ClientLoan(100000,24, client1, personal);
			ClientLoan clientLoan4 = new ClientLoan(200000,36, client3, automobile);
			client.addClientLoan(clientLoan1);
			client.addClientLoan(clientLoan2);
			client1.addClientLoan(clientLoan3);
			client3.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
		};
	}

}
