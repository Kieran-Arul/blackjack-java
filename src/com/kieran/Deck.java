package com.kieran;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private final ArrayList<Card> deck;

    public Deck() {

        this.deck = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {

            for (Card.Face face : Card.Face.values()) {
                this.deck.add(new Card(suit, face));
            }

        }

        Collections.shuffle(this.deck);

    }

    public Card deal() {
        return this.deck.remove(this.deck.size() - 1);
    }

}
