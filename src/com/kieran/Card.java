package com.kieran;

public class Card {

    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }

    public enum Face {

        TWO(2), THREE(3), FOUR(4), FIVE(5),
        SIX(6), SEVEN(7), EIGHT(8), NINE(9),
        TEN(10), JACK(10), QUEEN(10),
        KING(10), ACE(11);

        private final int value;

        Face(int value) {
            this.value = value;
        }

    }

    private final Suit suit;
    private final Face face;
    private int value;

    public Card(Suit suit, Face face) {
        this.face = face;
        this.suit = suit;
        this.value = face.value;
    }

    public int getValue() {
        return this.value;
    }

    public Face getFace() {
        return this.face;
    }

    public void changeAceValue() {

        if (this.face == Face.ACE) {
            this.value = 1;
        }

    }

    @Override
    public String toString() {
        return "The " + this.face + " of " + this.suit;
    }

}
