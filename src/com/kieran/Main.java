package com.kieran;

import java.util.Scanner;

public class Main {

    public enum PlayingStatus {
        DONE,
        PLAYING
    }

    private static final Player PLAYER = new Player("Player", false);
    private static final Player DEALER = new Player("Dealer", true);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Deck DECK = new Deck();
    private static int pot = 0;

    public static void main(String[] args) {

        boolean continuePlaying = true;

        while (continuePlaying) {

            System.out.println("Welcome " + PLAYER.getName() + ", you have " + PLAYER.getChips() + " chips");
            System.out.println("Welcome " + DEALER.getName() + ", you have " + DEALER.getChips() + " chips\n");

            boolean isValid = false;
            int bet = 0;

            while (!isValid) {

                System.out.println(PLAYER.getName() + ", enter your bet");
                bet = Integer.parseInt(SCANNER.nextLine());

                if ((bet <= DEALER.getChips()) && (inflatePot(PLAYER, bet) == 1)) {
                    isValid = true;
                }

            }

            inflatePot(DEALER, bet);

            dealCards();

            while (true) {

                if (hitPlayer(PLAYER) != PlayingStatus.PLAYING) {
                    break;
                }

            }

            while (true) {

                if (hitPlayer(DEALER) != PlayingStatus.PLAYING) {
                    break;
                }

            }

            compareHands();

            System.out.println("Continue playing? (y/n)");

            if (SCANNER.nextLine().equals("n")) {

                continuePlaying = false;
                System.out.println("Quitting game...");

            } else {

                System.out.println("Resetting game...");
                reset();

            }

        }

    }

    private static void dealCards() {

        for (int i = 0; i < 2; i++) {
            PLAYER.hit(DECK.deal());
            DEALER.hit(DECK.deal());
        }

        showCards(PLAYER);
        showOneCard();

    }

    private static int inflatePot(Player player, int betSize) {

        if (player.bet(betSize) != -1) {

            pot += betSize;
            System.out.println("\n" + player.getName() + " has bet " + betSize);
            System.out.println(player.getName() + " has " + player.getChips() + " chips remaining \n");

            return 1;

        }

        System.out.println("Invalid bet");

        return -1;

    }

    private static void winPot(Player player) {
        player.winBet(pot);
        pot = 0;
    }

    private static void splitPot() {
        Main.PLAYER.winBet(pot/2);
        Main.DEALER.winBet(pot/2);
        pot = 0;
    }

    private static boolean mustHit(Player player) {

        if ((player.evaluateHand() == 1) || player.evaluateHand() == -1) {
            return false;
        }

        return player.evaluateHand() < 18;

    }

    private static PlayingStatus isStillPlaying(Player.Status status) {

        if ((status == Player.Status.BLACKJACK) || (status == Player.Status.BUST)) {
            return PlayingStatus.DONE;
        }

        return PlayingStatus.PLAYING;

    }

    private static PlayingStatus hitPlayer(Player player) {

        if (mustHit(player)) {

            player.hit(DECK.deal());

            if (!player.isDealer()) {
                showCards(player);
            }

            return isStillPlaying(player.getStatus());

        } else {

            if (player.isDealer()) {
                return PlayingStatus.DONE;
            }

            System.out.println(player.getName() + ", would you like to hit? (y/n)");
            String choice = SCANNER.nextLine();

            if (choice.equals("y")) {

                player.hit(DECK.deal());
                showCards(player);

                return isStillPlaying(player.getStatus());

            } else {

                return PlayingStatus.DONE;

            }

        }

    }

    private static void showCards(Player player) {
        System.out.println("\n");
        System.out.println(player.getName() + " your cards are: ");
        player.showHand();
        System.out.println("\n");
    }

    private static void showOneCard() {
        System.out.println("\n");
        System.out.println("One of the dealer's cards are: ");
        Main.DEALER.showOne();
        System.out.println("\n");
    }

    private static void compareHands() {

        showCards(PLAYER);
        showCards(DEALER);

        if ((Main.PLAYER.getStatus() == Player.Status.BLACKJACK) && (Main.DEALER.getStatus() == Player.Status.BLACKJACK)) {
            splitPot();
            System.out.println("Both players obtained blackjack. Pot is split");
            return;
        }

        if ((Main.PLAYER.getStatus() == Player.Status.BUST) && (Main.DEALER.getStatus() == Player.Status.BUST)) {
            System.out.println("Both players busted. You both lose your bet");
            return;
        }

        if (Main.PLAYER.getStatus() == Player.Status.BLACKJACK) {
            System.out.println(PLAYER.getName() + " wins by blackjack");
            winPot(Main.PLAYER);
            return;
        }

        if (Main.DEALER.getStatus() == Player.Status.BLACKJACK) {
            System.out.println("Dealer wins by blackjack");
            winPot(Main.DEALER);
            return;
        }

        if (Main.PLAYER.getStatus() == Player.Status.BUST) {
            System.out.println("Dealer wins because " + PLAYER.getName() + " busted");
            winPot(Main.DEALER);
            return;
        }

        if (Main.DEALER.getStatus() == Player.Status.BUST) {
            System.out.println(PLAYER.getName() + " wins because dealer busted");
            winPot(Main.PLAYER);
            return;
        }

        if (Main.PLAYER.evaluateHand() > Main.DEALER.evaluateHand()) {
            System.out.println(PLAYER.getName() + " wins because his hand is higher than the dealer");
            winPot(Main.PLAYER);
            return;
        }

        if (Main.PLAYER.evaluateHand() < Main.DEALER.evaluateHand()) {
            System.out.println("Dealer wins because his hand is higher than " + PLAYER.getName());
            winPot(Main.DEALER);
            return;
        }

        if (Main.PLAYER.evaluateHand() == Main.DEALER.evaluateHand()) {
            splitPot();
            System.out.println("Both players hands are equal. Pot is split");
        }

    }

    private static void reset() {
        DECK = new Deck();
        pot = 0;
        PLAYER.getHand().clear();
        DEALER.getHand().clear();
        PLAYER.setStatus(Player.Status.ALIVE);
        DEALER.setStatus(Player.Status.ALIVE);
    }

}
