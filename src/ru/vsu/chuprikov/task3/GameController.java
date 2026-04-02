package ru.vsu.chuprikov.task3;

import java.io.*;
import java.util.*;

public class GameController {
    private Object player1Queue;
    private Object player2Queue;

    private boolean useCustomQueue;
    private List<String> gameLog;
    private int moveCount;

    String path = System.getProperty("user.dir") + "\\src\\ru\\vsu\\chuprikov\\task3\\";

    public GameController(boolean useCustomQueue) {
        this.useCustomQueue = useCustomQueue;
        this.gameLog = new ArrayList<>();
        this.moveCount = 0;
        initializeQueues();
    }

    private void initializeQueues() {
        if (useCustomQueue) {
            player1Queue = new MyQueue<Card>();
            player2Queue = new MyQueue<Card>();
        } else {
            player1Queue = new LinkedList<>();
            player2Queue = new LinkedList<>();
        }
    }

    private void addToPlayer1(Card card) {
        if (useCustomQueue) {
            ((MyQueue<Card>) player1Queue).enqueue(card);
        } else {
            ((Queue<Card>) player1Queue).add(card);
        }
    }

    private void addToPlayer2(Card card) {
        if (useCustomQueue) {
            ((MyQueue<Card>) player2Queue).enqueue(card);
        } else {
            ((Queue<Card>) player2Queue).add(card);
        }
    }

    private Card pollFromPlayer1() throws QueueException {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player1Queue).dequeue();
        } else {
            return ((Queue<Card>) player1Queue).poll();
        }
    }

    private Card pollFromPlayer2() throws QueueException {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player2Queue).dequeue();
        } else {
            return ((Queue<Card>) player2Queue).poll();
        }
    }

    private int sizePlayer1() {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player1Queue).size();
        } else {
            return ((Queue<Card>) player1Queue).size();
        }
    }

    private int sizePlayer2() {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player2Queue).size();
        } else {
            return ((Queue<Card>) player2Queue).size();
        }
    }

    private boolean isPlayer1Empty() {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player1Queue).isEmpty();
        } else {
            return ((Queue<Card>) player1Queue).isEmpty();
        }
    }

    private boolean isPlayer2Empty() {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player2Queue).isEmpty();
        } else {
            return ((Queue<Card>) player2Queue).isEmpty();
        }
    }

    private String getPlayer1CardsString() {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player1Queue).toString();
        } else {
            return ((Queue<Card>) player1Queue).toString();
        }
    }

    private String getPlayer2CardsString() {
        if (useCustomQueue) {
            return ((MyQueue<Card>) player2Queue).toString();
        } else {
            return ((Queue<Card>) player2Queue).toString();
        }
    }

    public void loadFromFile(String filename) throws IOException {
        List<Card> player1Cards = new ArrayList<>();
        List<Card> player2Cards = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path + filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null && lineNumber < 2) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] cardStrings = line.split("\\s+");
                for (String cardStr : cardStrings) {
                    if (cardStr.isEmpty()) continue;

                    String rankStr;
                    String suitStr;

                    if (cardStr.startsWith("10")) {
                        rankStr = "10";
                        suitStr = cardStr.substring(2);
                    } else {
                        rankStr = cardStr.substring(0, 1);
                        suitStr = cardStr.substring(1);
                    }

                    int rank;
                    switch (rankStr) {
                        case "6": rank = 6; break;
                        case "7": rank = 7; break;
                        case "8": rank = 8; break;
                        case "9": rank = 9; break;
                        case "10": rank = 10; break;
                        case "В": rank = 11; break;
                        case "Д": rank = 12; break;
                        case "К": rank = 13; break;
                        case "Т": rank = 14; break;
                        default: rank = 6;
                    }

                    int suit;
                    switch (suitStr) {
                        case "♥": suit = 0; break;
                        case "♦": suit = 1; break;
                        case "♣": suit = 2; break;
                        case "♠": suit = 3; break;
                        default: suit = 0;
                    }

                    Card card = new Card(rank, suit);

                    if (lineNumber == 0) {
                        player1Cards.add(card);
                    } else {
                        player2Cards.add(card);
                    }
                }
                lineNumber++;
            }
        }

        initializeQueues();
        for (Card card : player1Cards) {
            addToPlayer1(card);
        }
        for (Card card : player2Cards) {
            addToPlayer2(card);
        }

        gameLog.clear();
        moveCount = 0;
        gameLog.add("Игра загружена из файла: " + filename);
    }

    public void newGame() {
        List<Card> allCards = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                allCards.add(new Card(i + 6, j));
            }
        }

        Collections.shuffle(allCards);

        initializeQueues();
        for (int i = 0; i < allCards.size(); i++) {
            if (i % 2 == 0) {
                addToPlayer1(allCards.get(i));
            } else {
                addToPlayer2(allCards.get(i));
            }
        }

        gameLog.clear();
        moveCount = 0;
        gameLog.add("Новая игра начата!");
    }

    public String makeMove() throws QueueException {
        if (isGameOver()) {
            return "Игра окончена!";
        }

        moveCount++;
        Card card1 = pollFromPlayer1();
        Card card2 = pollFromPlayer2();

        if (card1 == null || card2 == null) {
            return "Ошибка: недостаточно карт!";
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("Ход %d: Игрок 1: %s, Игрок 2: %s", moveCount, card1, card2));

        if (card1.getValue() > card2.getValue()) {
            result.append(" -> Игрок 1 выиграл!");
            addToPlayer1(card1);
            addToPlayer1(card2);
            gameLog.add(result.toString());
            return result.toString();
        } else if (card2.getValue() > card1.getValue()) {
            result.append(" -> Игрок 2 выиграл!");
            addToPlayer2(card1);
            addToPlayer2(card2);
            gameLog.add(result.toString());
            return result.toString();
        } else {
            result.append(" -> СПОР!");
            gameLog.add(result.toString());
            return resolveConflict(card1, card2);
        }
    }

    private String resolveConflict(Card card1, Card card2) throws QueueException {
        StringBuilder conflictLog = new StringBuilder();
        conflictLog.append("СПОР!\n");

        List<Card> conflictCards = new ArrayList<>();
        conflictCards.add(card1);
        conflictCards.add(card2);

        while (true) {
            conflictLog.append("Выкладываем дополнительные карты.\n");

            Card extraCard1 = null;
            Card extraCard2 = null;

            if (sizePlayer1() == 0) {
                conflictLog.append("У игрока 1 нет карт! Игрок 2 забирает все карты спора.\n");
                for (Card c : conflictCards) {
                    addToPlayer2(c);
                }
                gameLog.add(conflictLog.toString());
                return conflictLog.toString();
            }

            if (sizePlayer2() == 0) {
                conflictLog.append("У игрока 2 нет карт! Игрок 1 забирает все карты спора.\n");
                for (Card c : conflictCards) {
                    addToPlayer1(c);
                }
                gameLog.add(conflictLog.toString());
                return conflictLog.toString();
            }

            if (sizePlayer1() == 1) {
                conflictLog.append("У игрока 1 осталась одна карта - спор идет без промежутка.\n");
                extraCard1 = pollFromPlayer1();
                conflictLog.append(String.format("Игрок 1 выкладывает: %s\n", extraCard1));
            } else {
                Card faceDown1 = pollFromPlayer1();
                extraCard1 = pollFromPlayer1();
                conflictLog.append(String.format("Игрок 1 выкладывает: %s рубашкой вверх, %s\n", faceDown1, extraCard1));
                conflictCards.add(faceDown1);
            }

            if (sizePlayer2() == 1) {
                conflictLog.append("У игрока 2 осталась одна карта - спор идет без промежутка.\n");
                extraCard2 = pollFromPlayer2();
                conflictLog.append(String.format("Игрок 2 выкладывает: %s\n", extraCard2));
            } else {
                Card faceDown2 = pollFromPlayer2();
                extraCard2 = pollFromPlayer2();
                conflictLog.append(String.format("Игрок 2 выкладывает: %s рубашкой вверх, %s\n", faceDown2, extraCard2));
                conflictCards.add(faceDown2);
            }

            conflictCards.add(extraCard1);
            conflictCards.add(extraCard2);

            conflictLog.append(String.format("Сравниваем: %s и %s\n", extraCard1, extraCard2));

            if (extraCard1.getValue() > extraCard2.getValue()) {
                conflictLog.append("Игрок 1 выигрывает спор!");
                for (Card c : conflictCards) {
                    addToPlayer1(c);
                }
                break;
            } else if (extraCard2.getValue() > extraCard1.getValue()) {
                conflictLog.append("Игрок 2 выигрывает спор!");
                for (Card c : conflictCards) {
                    addToPlayer2(c);
                }
                break;
            } else {
                conflictLog.append("Снова ничья! Продолжаем спор.\n");
            }
        }

        gameLog.add(conflictLog.toString());
        return conflictLog.toString();
    }

    public boolean isGameOver() {
        return isPlayer1Empty() || isPlayer2Empty();
    }

    public String getWinner() {
        if (!isGameOver()) return null;
        if (isPlayer1Empty()) return "Игрок 2";
        return "Игрок 1";
    }

    public String getPlayer1Cards() {
        return getPlayer1CardsString();
    }

    public String getPlayer2Cards() {
        return getPlayer2CardsString();
    }

    public int getPlayer1Count() {
        return sizePlayer1();
    }

    public int getPlayer2Count() {
        return sizePlayer2();
    }

    public List<String> getGameLog() {
        return gameLog;
    }

    public boolean isUsingCustomQueue() {
        return useCustomQueue;
    }
}