package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void saveAccountService(Account account){
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> getAllAccountService(){
        return accountRepository.findAll()
                                .stream()
                                .map(acc -> new AccountDTO(acc))
                                .collect(Collectors.toList());
    }

    @Override
    public Account getOneAccountService(Long id, Client client){
        return accountRepository.findByIdAndClient(id, client);
    }

    @Override
    public Account findAccountNumberService(String accountNumber){
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public boolean existsByNumberAndClientService(String number, Client client){
        return accountRepository.existsByNumberAndClient(number, client);
    }

    @Override
    public boolean existByNumberService(String number){
        return accountRepository.existsByNumber(number);
    }
}
