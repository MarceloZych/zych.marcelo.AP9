package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args) -> {
			// client
			Client client = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(client);
			// client accounts
			Account account = new Account("VIN001", LocalDate.now(), 5000);
			Account account1 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			client.addAccountSet(account);
			client.addAccountSet(account1);
			accountRepository.save(account);
			accountRepository.save(account1);
			// client2 transactions
			Transaction transaction = new Transaction( 100, "SuperMarket shopping", LocalDate.now(), TransactionType.DEBIT);
			Transaction transaction1 = new Transaction( 200, "SuperMarket shopping", LocalDate.now(), TransactionType.CREDIT);
			account.addTransactionSet(transaction);
			account1.addTransactionSet(transaction1);
			transactionRepository.save(transaction);
			transactionRepository.save(transaction1);


			// client 1
			Client client1 = new Client("Marcelo", "Zych", "mz@mindhub.com");
			clientRepository.save(client1);
			// client1 accounts
			Account account2 = new Account("VIN003", LocalDate.now(), 15000);
			Account account3 = new Account("VIN004", LocalDate.now().plusDays(1), 17500);
			client1.addAccountSet(account2);
			client1.addAccountSet(account3);
			accountRepository.save(account2);
			accountRepository.save(account3);
			// client2 transactions
			Transaction transaction2 = new Transaction( 10, "Clothes shopping", LocalDate.now(), TransactionType.DEBIT);
			Transaction transaction3 = new Transaction( 50, "Tecnology shopping", LocalDate.now(), TransactionType.CREDIT);
			account2.addTransactionSet(transaction2);
			account3.addTransactionSet(transaction3);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			// client 2
			Client client3 = new Client("Maria", "Sanches", "mary@mindhub.com");
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
			account4.addTransactionSet(transaction4);
			account5.addTransactionSet(transaction5);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);

		};
	}

}
