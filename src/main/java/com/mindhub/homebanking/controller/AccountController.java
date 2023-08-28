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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccount (){
        List<Account> allAccounts = accountRepository.findAll();

        List<AccountDTO> accountDTOSConvertedList = allAccounts
                                                        .stream()
                                                        .map(acc -> new AccountDTO(acc))
                                                        .collect(Collectors.toList());
        return accountDTOSConvertedList;
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getOneAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null);
        return new AccountDTO(account);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        // Verificar si el cliente ya tiene 3 cuentas registradas
        if (client.getAccountSet().size() >= 3) {
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
