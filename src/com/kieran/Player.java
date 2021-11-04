package com.kieran;

import java.util.ArrayList;

public class Player {

    public enum Status {
        BLACKJACK,
        BUST,
        ALIVE
    }

    private final String name;
    private final ArrayList<Card> hand;
    private final boolean isDealer;
    private int chips;
    private Status status;

    public Player(String name, boolean isDealer) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isDealer = isDealer;
        this.chips = 100000;
        this.status = Status.ALIVE;
    }

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getChips() {
        return this.chips;
    }

    public boolean isDealer() {
        return this.isDealer;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    private int getRawHandValue() {

        int value = 0;

        for (Card card : this.hand) {
            value += card.getValue();
        }

        return value;

    }

    public int evaluateHand() {

        if (isBlackJack()) {
            return 1;
        }

        if (isBust()) {

            for (Card card : this.hand) {
                card.changeAceValue();
            }

            if (isBust()) {
                return -1;
            }

            return getRawHandValue();

        }

        return getRawHandValue();

    }

    private boolean isBlackJack() {

        if (this.hand.size() == 2) {
            return ((this.hand.get(0).getValue() + this.hand.get(1).getValue()) == 21) ||
                    ((this.hand.get(0).getValue() + this.hand.get(1).getValue()) == 22);
        }

        return false;

    }

    private boolean isBust() {
        return getRawHandValue() > 21;
    }

    public void hit(Card card) {

        this.hand.add(card);

        int evalHandScore = evaluateHand();

        if (evalHandScore == 1) {

            this.status = Status.BLACKJACK;

        } else if (evalHandScore == -1) {

            this.status = Status.BUST;

        }

    }

    public int bet(int betSize) {

        if ((this.chips >= betSize) && (betSize > 0)) {

            this.chips -= betSize;

            return 1;

        }

        return -1;

    }

    public void winBet(int bet) {
        this.chips += bet;
    }

    public void showHand() {

        for (Card card : this.hand) {
            System.out.println(card);
        }

    }

    public void showOne() {
        System.out.println(this.hand.get(1));
    }

}
