package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/clients/current")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            @RequestBody CardDTO cardDTO,
            Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        if (cardRepository.countByClientAndType(client, cardDTO.getType()) >= 3){
            return new ResponseEntity<>("You can't have more cards of this type", HttpStatus.FORBIDDEN);
        }

        if (cardRepository.existsByClientAndTypeAndColor(client, cardDTO.getType(), cardDTO.getColor())){
            return new ResponseEntity<>("You can't have more cards of this color", HttpStatus.FORBIDDEN);
        }

        String cardNumber = Card.generateCardNumber(cardRepository);
        int cvvNumber = Card.generateRandomNumber(100, 999);

        Card card = new Card();
        card.setType(cardDTO.getType());
        card.setColor(cardDTO.getColor());
        card.setNumber(cardNumber);
        card.setCvv(cvvNumber);
        card.setFromDate(LocalDate.now());
        card.setThruDate(LocalDate.now().plusYears(5));
        card.setCardHolder(client.getFirstName() + " " + client.getLastName());
        card.setClient(client);

        cardRepository.save(card);


        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }
}
