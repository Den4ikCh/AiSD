package ru.vsu.chuprikov.task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class WindowApp extends JFrame {
    private String path = System.getProperty("user.dir") + "\\src\\ru\\vsu\\chuprikov\\task3\\";

    private GameController gameController;
    private JTextArea player1Area;
    private JTextArea player2Area;
    private JTextArea logArea;
    private JLabel player1CountLabel;
    private JLabel player2CountLabel;
    private JButton moveButton;
    private JButton newGameButton;
    private JButton loadButton;
    private JRadioButton customQueueRadio;
    private JRadioButton builtInQueueRadio;
    private ButtonGroup queueGroup;

    public WindowApp() {
        setTitle("Карточная игра Пьяница");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        createComponents();
        layoutComponents();
        attachEvents();

        gameController = new GameController(true);
        gameController.newGame();
        updateDisplay();

        setVisible(true);
    }

    private void createComponents() {
        player1Area = new JTextArea();
        player1Area.setEditable(false);
        player1Area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        player2Area = new JTextArea();
        player2Area.setEditable(false);
        player2Area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        player1CountLabel = new JLabel("Карт: 0");
        player2CountLabel = new JLabel("Карт: 0");

        moveButton = new JButton("Сделать ход");
        newGameButton = new JButton("Новая игра");
        loadButton = new JButton("Загрузить из файла");

        customQueueRadio = new JRadioButton("СВОЯ очередь (связный список)");
        builtInQueueRadio = new JRadioButton("СТАНДАРТНАЯ очередь Java");
        builtInQueueRadio.setSelected(true);
        queueGroup = new ButtonGroup();
        queueGroup.add(customQueueRadio);
        queueGroup.add(builtInQueueRadio);

        moveButton.setBackground(new Color(100, 200, 100));
        newGameButton.setBackground(new Color(200, 200, 100));
        loadButton.setBackground(new Color(100, 150, 200));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topPanel.add(customQueueRadio);
        topPanel.add(builtInQueueRadio);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel player1Panel = new JPanel(new BorderLayout());
        player1Panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE), "Карты Игрока 1"));

        JScrollPane player1Scroll = new JScrollPane(player1Area);
        player1Scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        player1Panel.add(player1Scroll, BorderLayout.CENTER);

        JPanel player1CountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        player1CountPanel.add(player1CountLabel);
        player1Panel.add(player1CountPanel, BorderLayout.SOUTH);

        JPanel player2Panel = new JPanel(new BorderLayout());
        player2Panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.RED), "Карты Игрока 2"));

        JScrollPane player2Scroll = new JScrollPane(player2Area);
        player2Scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        player2Panel.add(player2Scroll, BorderLayout.CENTER);

        JPanel player2CountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        player2CountPanel.add(player2CountLabel);
        player2Panel.add(player2CountPanel, BorderLayout.SOUTH);

        centerPanel.add(player1Panel);
        centerPanel.add(player2Panel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(moveButton);
        buttonPanel.add(newGameButton);
        buttonPanel.add(loadButton);

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Лог игры"));

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.setPreferredSize(new Dimension(850, 200));
        logPanel.add(logScrollPane, BorderLayout.CENTER);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(logPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        moveButton.addActionListener((ActionEvent e) -> {
            if (gameController.isGameOver()) {
                JOptionPane.showMessageDialog(this,
                        "Игра окончена! Победил: " + gameController.getWinner(),
                        "Конец игры",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                gameController.makeMove();
            } catch (QueueException ex) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
            updateDisplay();

            if (gameController.isGameOver()) {
                JOptionPane.showMessageDialog(this,
                        "Игра окончена! Победил: " + gameController.getWinner() +
                                "\nВсего ходов: " + gameController.getGameLog().size(),
                        "Конец игры",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        newGameButton.addActionListener((ActionEvent e) -> {
            boolean useBuiltIn = builtInQueueRadio.isSelected();
            gameController = new GameController(useBuiltIn);
            gameController.newGame();
            updateDisplay();

            String queueType = useBuiltIn ? "стандартной очереди Java" : "собственной очереди";
            JOptionPane.showMessageDialog(this,
                    "Начата новая игра с использованием " + queueType,
                    "Новая игра",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        loadButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser(path);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory() || file.getName().toLowerCase().endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    return "Текстовые файлы (*.txt)";
                }
            });
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    boolean useBuiltIn = builtInQueueRadio.isSelected();
                    gameController = new GameController(useBuiltIn);
                    gameController.loadFromFile(fileChooser.getSelectedFile().getName());
                    updateDisplay();
                    JOptionPane.showMessageDialog(this,
                            "Игра загружена из файла",
                            "Загрузка",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Ошибка загрузки файла: " + ex.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateDisplay() {
        player1Area.setText(gameController.getPlayer1Cards());
        player2Area.setText(gameController.getPlayer2Cards());
        player1CountLabel.setText("Карт: " + gameController.getPlayer1Count());
        player2CountLabel.setText("Карт: " + gameController.getPlayer2Count());

        StringBuilder logText = new StringBuilder();
        for (String log : gameController.getGameLog()) {
            logText.append(log).append("\n");
        }
        logArea.setText(logText.toString());
        logArea.setCaretPosition(logArea.getDocument().getLength());

        moveButton.setEnabled(!gameController.isGameOver());

        validate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new WindowApp();
        });
    }
}