package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Card;

public interface CardService {

    boolean existsCardService(Client client, CardType cardType, CardColor cardColor);

    void saveCardService(Card card);


}
