package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.CardRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private String cardHolder;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Card() {
    }

    public Card(CardType type, CardColor color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, String cardHolder) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardHolder = cardHolder;
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Client getClient(){ return client; }

    public void setClient(Client client){
        this.client = client;
    }

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