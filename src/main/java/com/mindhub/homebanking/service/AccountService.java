package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    void saveAccountService(Account account);

    List<AccountDTO> getAllAccountService();

    Account getOneAccountService(Long id, Client client);

    Account findAccountNumberService(String accountNumber);

    boolean existsByNumberAndClientService(String number, Client client);

    boolean existByNumberService(String number);
}
