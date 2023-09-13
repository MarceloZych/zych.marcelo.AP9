package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public boolean existsCardService(Client client, CardType cardType, CardColor cardColor){
        return cardRepository.existsByClientAndTypeAndColor(client, cardType, cardColor);
    }
    @Override
    public void saveCardService(Card card){
        cardRepository.save(card);
    }
    
}
