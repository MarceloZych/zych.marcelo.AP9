package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccount (){

        return accountService.getAllAccountService();
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccount (Authentication authentication){
        Client client = clientService.findClientByEmailService(authentication.getName());

        List<AccountDTO> accountDTOS = client.getAccounts()
                .stream()
                .map(acc -> new AccountDTO(acc))
                .collect(Collectors.toList());

        return accountDTOS;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getOneAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.findClientByEmailService(authentication.getName());
        Account account = accountService.getOneAccountService(id, client);

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

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientService.findClientByEmailService(authentication.getName());

        // Verificar si el cliente ya tiene 3 cuentas registradas
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You can't have more than 3 accounts.", HttpStatus.FORBIDDEN);
        }

        // Crear la nueva cuenta
        String checkingAccountNumber = AccountUtils.generateAccountNumber(accountRepository);

        Account account = new Account(checkingAccountNumber, LocalDateTime.now(),0.0);
        client.addAccountSet(account);
        accountService.saveAccountService(account);

        return new ResponseEntity<>("Account created successfully.", HttpStatus.CREATED);
    }


}
