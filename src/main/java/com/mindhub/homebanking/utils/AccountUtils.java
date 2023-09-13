package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;

public class AccountUtils {
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
