package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

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
}
