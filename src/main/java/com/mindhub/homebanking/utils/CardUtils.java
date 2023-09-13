package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;
public class CardUtils {
    public static String generateCardNumber(CardRepository cardRepository) {
        String cardNumber = "";
        do {
            StringBuilder cardNumberBuilder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                cardNumberBuilder.append(generateRandomNumber(1000, 9999));
                if (i < 3) {
                    cardNumberBuilder.append("-");
                }
            }
            cardNumber = cardNumberBuilder.toString();
        } while (cardRepository.existsByNumber(cardNumber));

        return cardNumber;
    }

    public static int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
