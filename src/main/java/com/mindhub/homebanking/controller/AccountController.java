package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAllAccount (){
        List<Account> allAccounts = accountRepository.findAll();

        Set<AccountDTO> accountDTOS = allAccounts
                .stream()
                .map(acc -> new AccountDTO(acc))
                .collect(Collectors.toSet());
        return accountDTOS;
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount (Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        Set<AccountDTO> accountDTOS = client.getAccounts()
                .stream()
                .map(acc -> new AccountDTO(acc))
                .collect(Collectors.toSet());
        return accountDTOS;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getOneAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);

        if(account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.BAD_GATEWAY);
        }

        if( account.getClient().equals(client)){
            AccountDTO accountDTO = new AccountDTO(account);
            return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>("Account is not your", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        // Verificar si el cliente ya tiene 3 cuentas registradas
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You can't have more than 3 accounts.", HttpStatus.FORBIDDEN);
        }

        // Crear la nueva cuenta
        String checkingAccountNumber = Account.generateAccountNumber(accountRepository);

        Account account = new Account(checkingAccountNumber, LocalDate.now(),0.0);
        client.addAccountSet(account);
        accountRepository.save(account);

        return new ResponseEntity<>("Account created successfully.", HttpStatus.CREATED);
    }


}
