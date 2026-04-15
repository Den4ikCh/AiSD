package ru.vsu.chuprikov.task4;

import java.io.*;

public class FileUtils {

    public static SortedData<?> readDataFromFile(String filePath) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String orderValuesLine = reader.readLine();
            if (orderValuesLine == null) {
                throw new IllegalArgumentException("Файл пуст");
            }

            String dataLine = reader.readLine();
            if (dataLine == null) {
                throw new IllegalArgumentException("Файл не содержит строку с данными");
            }

            String[] orderValuesStr = orderValuesLine.trim().split("\\s+");
            int[] orderValues = new int[orderValuesStr.length];
            for (int i = 0; i < orderValuesStr.length; i++) {
                orderValues[i] = Integer.parseInt(orderValuesStr[i]);
            }

            String[] dataValues = dataLine.trim().split("\\s+");

            if (orderValues.length != dataValues.length) {
                throw new IllegalArgumentException(
                        String.format("Длины массивов не совпадают: ключей=%d, значений=%d",
                                orderValues.length, dataValues.length)
                );
            }

            Object[] dataArray = parseDataArray(dataValues);
            return new SortedData<>(dataArray, orderValues);
        }
    }

    private static <T> T[] parseDataArray(String[] stringValues) {
        boolean allIntegers = true;
        for (String value : stringValues) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                allIntegers = false;
                break;
            }
        }

        if (allIntegers) {
            Integer[] result = new Integer[stringValues.length];
            for (int i = 0; i < stringValues.length; i++) {
                result[i] = Integer.parseInt(stringValues[i]);
            }
            return (T[]) result;
        }

        boolean allDoubles = true;
        for (String value : stringValues) {
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                allDoubles = false;
                break;
            }
        }

        if (allDoubles) {
            Double[] result = new Double[stringValues.length];
            for (int i = 0; i < stringValues.length; i++) {
                result[i] = Double.parseDouble(stringValues[i]);
            }
            return (T[]) result;
        }

        return (T[]) stringValues;
    }

    public static <T> void writeResultToFile(String filePath, T[] data, int[] orderValues) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < orderValues.length; i++) {
                writer.write(String.valueOf(orderValues[i]));
                if (i < orderValues.length - 1) {
                    writer.write(" ");
                }
            }
            writer.newLine();

            for (int i = 0; i < data.length; i++) {
                writer.write(String.valueOf(data[i]));
                if (i < data.length - 1) {
                    writer.write(" ");
                }
            }
            writer.newLine();
        }
    }
}
