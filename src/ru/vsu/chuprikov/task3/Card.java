package ru.vsu.chuprikov.task3;

public class Card {
    private final String suit;
    private final String rank;
    private final int value;

    private static final String[] RANKS = {"6", "7", "8", "9", "10", "В", "Д", "К", "Т"};
    private static final String[] SUITS = {"♥", "♦", "♣", "♠"};

    public Card(int rank, int suitIndex) {
        this.rank = RANKS[rank - 6];
        this.suit = SUITS[suitIndex];
        this.value = rank;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return rank + suit;
    }
}
