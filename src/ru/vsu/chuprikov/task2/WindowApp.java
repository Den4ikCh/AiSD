package ru.vsu.chuprikov.task2;

import ru.vsu.chuprikov.utils.ConsoleUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WindowApp extends JFrame {
    private String path = System.getProperty("user.dir") + "\\src\\ru\\vsu\\chuprikov\\task2\\";
    private JTextArea inputArea1;
    private JTextArea inputArea2;
    private JTextArea resultArea;
    private JButton loadFromFile;
    private JButton saveToFile;
    private JButton printResult;

    public WindowApp() {
        setTitle("Объединение двух отсортированных списков");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout());
        loadFromFile = new JButton("Загрузить пример");
        saveToFile = new JButton("Сохранить");
        printResult = new JButton("Объединить списки");
        controlPanel.add(loadFromFile);
        controlPanel.add(saveToFile);
        controlPanel.add(printResult);

        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Первый список
        JPanel inputPanel1 = new JPanel(new BorderLayout());
        inputPanel1.setBorder(BorderFactory.createTitledBorder("Список 1 (числа через пробел):"));
        inputArea1 = new JTextArea(5, 50);
        inputArea1.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputPanel1.add(new JScrollPane(inputArea1), BorderLayout.CENTER);

        // Второй список
        JPanel inputPanel2 = new JPanel(new BorderLayout());
        inputPanel2.setBorder(BorderFactory.createTitledBorder("Список 2 (числа через пробел):"));
        inputArea2 = new JTextArea(5, 50);
        inputArea2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputPanel2.add(new JScrollPane(inputArea2), BorderLayout.CENTER);

        listsPanel.add(inputPanel1);
        listsPanel.add(inputPanel2);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Результат:"));
        resultArea = new JTextArea(5, 50);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(listsPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);
        add(mainPanel);

        loadFromFile.addActionListener(e -> loadFromFile());
        saveToFile.addActionListener(e -> saveToFile());
        printResult.addActionListener(e -> printResult());
    }

    private void loadFromFile() {
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
                SortedLinkedList<Number>[] lists = ConsoleUtils.readTwoListsFromFile(fileChooser.getSelectedFile().getPath());

                inputArea1.setText(formatList(lists[0]));
                inputArea2.setText(formatList(lists[1]));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки файла: " + ex.getMessage());
            }
        }
    }

    private String formatList(SortedLinkedList<Number> list) {
        StringBuilder sb = new StringBuilder();
        for (Number num : list) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(num.toString());
        }
        return sb.toString();
    }

    private void saveToFile() {
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

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getAbsolutePath() + ".txt");
                }

                SortedLinkedList<Number> result = readListFromWindow();
                ConsoleUtils.printList(file.getPath(), result);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения файла: " + ex.getMessage());
            }
        }
    }

    private void printResult() {
        try {
            SortedLinkedList<Number> list1 = parseList(inputArea1.getText());
            SortedLinkedList<Number> list2 = parseList(inputArea2.getText());

            SortedLinkedList<Number> result = SortedLinkedList.combine(list1, list2);
            resultArea.setText(result.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage());
        }
    }

    private SortedLinkedList<Number> readListFromWindow() throws SortedLinkedListException {
        SortedLinkedList<Number> list = new SortedLinkedList<>();
        String text = resultArea.getText().replace("[", "").replace("]", "").replace(",", "");
        String[] tokens = text.trim().split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            try {
                list.addLast(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                list.addLast(Double.parseDouble(token));
            }
        }
        return list;
    }

    private SortedLinkedList<Number> parseList(String text) throws SortedLinkedListException {
        SortedLinkedList<Number> list = new SortedLinkedList<>();
        String[] tokens = text.trim().split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            try {
                list.addLast(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                list.addLast(Double.parseDouble(token));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WindowApp().setVisible(true);
            }
        });
    }
}