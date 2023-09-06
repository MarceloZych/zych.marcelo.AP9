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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)

    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor,
            Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (cardRepository.existsByClientAndTypeAndColor(client, cardType, cardColor)){
            return new ResponseEntity<>("You can't have more cards of this color", HttpStatus.FORBIDDEN);
        }

        String cardNumber = Card.generateCardNumber(cardRepository);
        int cvvNumber = Card.generateRandomNumber(100, 999);

        Card card = new Card(
                            cardType,
                            cardColor,
                            cardNumber,
                            cvvNumber,
                            LocalDateTime.now(),
                            LocalDateTime.now().plusYears(5),
                            client.getFirstName() + " " + client.getLastName()
        );

        client.addCard(card);

        cardRepository.save(card);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

        @GetMapping(value = "/clients/current/cards")
        public ResponseEntity<Object> cardClient (Authentication authentication){
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client != null) {
                Set<Card> cards = cardRepository.findByClient(client);
                Set<CardDTO> cardDTOs = cards.stream()
                        .map(clientCards -> new CardDTO(clientCards))
                        .collect(Collectors.toSet());

                return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }

}
