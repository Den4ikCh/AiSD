package ru.vsu.chuprikov.task4;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WindowApp extends JFrame {
    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/ru/vsu/chuprikov/task4/";

    private JTextArea originalKeysArea;
    private JTextArea originalDataArea;
    private JTextArea sortedKeysArea;
    private JTextArea sortedDataArea;
    private JLabel dataTypeLabel;

    private JButton loadButton;
    private JButton sortButton;
    private JButton saveButton;
    private JButton clearButton;

    private SortedData<?> currentData;
    private SortedData<?> sortedData;

    public WindowApp() {
        setTitle("Пузырьковая сортировка двух массивов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        createComponents();
        layoutComponents();
        attachEvents();

        setVisible(true);
    }

    private void createComponents() {
        // Текстовые области для исходных данных
        originalKeysArea = new JTextArea(5, 30);
        originalKeysArea.setEditable(true);
        originalKeysArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        originalKeysArea.setBorder(BorderFactory.createTitledBorder("Исходные ключи (целые числа через пробел)"));

        originalDataArea = new JTextArea(5, 30);
        originalDataArea.setEditable(true);
        originalDataArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        originalDataArea.setBorder(BorderFactory.createTitledBorder("Исходные данные (автоопределение типа)"));

        // Текстовые области для отсортированных данных
        sortedKeysArea = new JTextArea(5, 30);
        sortedKeysArea.setEditable(false);
        sortedKeysArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sortedKeysArea.setBorder(BorderFactory.createTitledBorder("Отсортированные ключи"));

        sortedDataArea = new JTextArea(5, 30);
        sortedDataArea.setEditable(false);
        sortedDataArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sortedDataArea.setBorder(BorderFactory.createTitledBorder("Отсортированные данные"));

        // Информационная метка для типа данных
        dataTypeLabel = new JLabel("Тип данных: не определен");
        dataTypeLabel.setForeground(Color.BLUE);
        dataTypeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Кнопки
        loadButton = new JButton("Загрузить из файла");
        sortButton = new JButton("Сортировать");
        saveButton = new JButton("Сохранить результат");
        clearButton = new JButton("Очистить");

        // Стилизация кнопок
        loadButton.setBackground(new Color(100, 150, 200));
        sortButton.setBackground(new Color(100, 200, 100));
        saveButton.setBackground(new Color(200, 200, 100));
        clearButton.setBackground(new Color(200, 150, 150));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // Верхняя панель с управлением
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topPanel.add(loadButton);
        topPanel.add(sortButton);
        topPanel.add(saveButton);
        topPanel.add(clearButton);
        topPanel.add(dataTypeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Центральная панель с данными
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane originalKeysScroll = new JScrollPane(originalKeysArea);
        JScrollPane originalDataScroll = new JScrollPane(originalDataArea);
        JScrollPane sortedKeysScroll = new JScrollPane(sortedKeysArea);
        JScrollPane sortedDataScroll = new JScrollPane(sortedDataArea);

        centerPanel.add(originalKeysScroll);
        centerPanel.add(originalDataScroll);
        centerPanel.add(sortedKeysScroll);
        centerPanel.add(sortedDataScroll);

        add(centerPanel, BorderLayout.CENTER);

        // Нижняя панель с информацией
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JLabel infoLabel = new JLabel("Введите данные в левые поля или загрузите из файла, затем нажмите 'Сортировать'");
        infoLabel.setForeground(Color.GRAY);
        bottomPanel.add(infoLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        loadButton.addActionListener(e -> loadFromFile());
        sortButton.addActionListener(e -> performSort());
        saveButton.addActionListener(e -> saveToFile());
        clearButton.addActionListener(e -> clearAll());
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser(BASE_PATH);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы", "txt"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                currentData = FileUtils.readDataFromFile(filePath);

                // Отображаем загруженные данные
                originalKeysArea.setText(arrayToString(currentData.orderValues));
                originalDataArea.setText(arrayToString(currentData.data));

                // Определяем и отображаем тип данных
                String dataType = detectDataType(currentData.data);
                dataTypeLabel.setText("Тип данных: " + dataType);
                dataTypeLabel.setForeground(Color.BLUE);

                // Очищаем отсортированные данные
                sortedKeysArea.setText("");
                sortedDataArea.setText("");
                sortedData = null;

                JOptionPane.showMessageDialog(this,
                        "Данные успешно загружены из файла\nТип данных: " + dataType,
                        "Загрузка",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка загрузки файла: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performSort() {
        try {
            // Получаем данные из полей ввода
            String keysText = originalKeysArea.getText().trim();
            String dataText = originalDataArea.getText().trim();

            if (keysText.isEmpty() || dataText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Пожалуйста, введите данные или загрузите из файла",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Парсим ключи
            String[] keysStr = keysText.split("\\s+");
            int[] orderValues = new int[keysStr.length];
            for (int i = 0; i < keysStr.length; i++) {
                orderValues[i] = Integer.parseInt(keysStr[i]);
            }

            // Парсим данные с автоопределением типа
            String[] dataValues = dataText.split("\\s+");

            if (orderValues.length != dataValues.length) {
                JOptionPane.showMessageDialog(this,
                        String.format("Количество ключей (%d) и данных (%d) не совпадает!",
                                orderValues.length, dataValues.length),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Автоматически определяем тип и парсим данные
            Object[] dataArray = parseDataArrayAuto(dataValues);
            String detectedType = detectDataType(dataArray);
            dataTypeLabel.setText("Тип данных: " + detectedType);
            dataTypeLabel.setForeground(new Color(0, 100, 0));

            // Сортируем
            int[] orderValuesCopy = orderValues.clone();
            Object[] dataCopy = dataArray.clone();
            BubbleSort.sort(dataCopy, orderValuesCopy);

            // Отображаем результат
            sortedKeysArea.setText(arrayToString(orderValuesCopy));
            sortedDataArea.setText(arrayToString(dataCopy));

            // Сохраняем для возможного сохранения в файл с правильным типом
            if (dataCopy instanceof Integer[]) {
                sortedData = new SortedData<>((Integer[]) dataCopy, orderValuesCopy);
            } else if (dataCopy instanceof Double[]) {
                sortedData = new SortedData<>((Double[]) dataCopy, orderValuesCopy);
            } else {
                sortedData = new SortedData<>((String[]) dataCopy, orderValuesCopy);
            }

            JOptionPane.showMessageDialog(this,
                    "Сортировка выполнена успешно!\nТип данных: " + detectedType,
                    "Результат",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка формата чисел в ключах: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при сортировке: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Object[] parseDataArrayAuto(String[] stringValues) {
        if (stringValues.length == 0) {
            return new Object[0];
        }

        // Проверяем, можно ли все значения преобразовать в Integer
        boolean allIntegers = true;
        for (String value : stringValues) {
            try {
                Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                allIntegers = false;
                break;
            }
        }

        if (allIntegers) {
            Integer[] result = new Integer[stringValues.length];
            for (int i = 0; i < stringValues.length; i++) {
                result[i] = Integer.parseInt(stringValues[i].trim());
            }
            return result;
        }

        // Проверяем, можно ли все значения преобразовать в Double
        boolean allDoubles = true;
        for (String value : stringValues) {
            try {
                Double.parseDouble(value.trim());
            } catch (NumberFormatException e) {
                allDoubles = false;
                break;
            }
        }

        if (allDoubles) {
            Double[] result = new Double[stringValues.length];
            for (int i = 0; i < stringValues.length; i++) {
                result[i] = Double.parseDouble(stringValues[i].trim());
            }
            return result;
        }

        // Иначе оставляем как строки
        return stringValues;
    }

    private void saveToFile() {
        if (sortedData == null) {
            JOptionPane.showMessageDialog(this,
                    "Нет отсортированных данных для сохранения. Сначала выполните сортировку.",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser(BASE_PATH);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы", "txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".txt")) {
                    filePath += ".txt";
                }

                FileUtils.writeResultToFile(filePath, sortedData.data, sortedData.orderValues);

                JOptionPane.showMessageDialog(this,
                        "Результат сохранен в файл: " + filePath,
                        "Сохранение",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка сохранения файла: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearAll() {
        originalKeysArea.setText("");
        originalDataArea.setText("");
        sortedKeysArea.setText("");
        sortedDataArea.setText("");
        currentData = null;
        sortedData = null;
        dataTypeLabel.setText("Тип данных: не определен");
        dataTypeLabel.setForeground(Color.BLUE);
    }

    private String detectDataType(Object[] data) {
        if (data.length == 0) return "не определен";
        if (data[0] instanceof Integer) return "Integer";
        if (data[0] instanceof Double) return "Double";
        if (data[0] instanceof String) return "String";
        return "не определен";
    }

    private String arrayToString(int[] array) {
        if (array == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    private String arrayToString(Object[] array) {
        if (array == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(" ");
        }
        return sb.toString();
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