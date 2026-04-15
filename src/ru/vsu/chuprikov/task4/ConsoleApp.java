package ru.vsu.chuprikov.task4;

import ru.vsu.chuprikov.utils.ConsoleUtils;
import ru.vsu.chuprikov.utils.InputArgs;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ConsoleApp {
    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/ru/vsu/chuprikov/task4/";

    public static void main(String[] args) {
        InputArgs inputArgs = ConsoleUtils.parseCmdArgs(args);

        String inputFile = BASE_PATH + inputArgs.inputFile;
        String outputFile = BASE_PATH + inputArgs.outputFile;

        Class<?> dataType = detectDataType(args);

        try {
            System.out.println("Чтение данных из файла: " + inputFile);
            SortedData<?> sortedData = FileUtils.readDataFromFile(inputFile);

            System.out.println("Исходные данные:");
            System.out.println("Ключи: " + arrayToString(sortedData.orderValues));
            System.out.println("Данные: " + arrayToString(sortedData.data));

            sortData(sortedData);

            System.out.println("\nРезультат сортировки:");
            System.out.println("Ключи: " + arrayToString(sortedData.orderValues));
            System.out.println("Данные: " + arrayToString(sortedData.data));

            FileUtils.writeResultToFile(outputFile, sortedData.data, sortedData.orderValues);
            System.out.println("\nРезультат сохранен в файл: " + outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл не найден - " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка в данных: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void sortData(SortedData<T> sortedData) {
        BubbleSort.sort(sortedData.data, sortedData.orderValues);
    }

    private static Class<?> detectDataType(String[] args) {
        Class<?> dataType = String.class;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("--data-type=")) {
                String type = arg.substring("--data-type=".length());
                dataType = getDataTypeFromString(type);
            }
            else if (arg.equals("--data-type") && i + 1 < args.length) {
                String type = args[i + 1];
                dataType = getDataTypeFromString(type);
            }
        }

        return dataType;
    }

    private static Class<?> getDataTypeFromString(String type) {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return Integer.class;
            case "double":
                return Double.class;
            case "string":
            default:
                return String.class;
        }
    }

    private static String arrayToString(int[] array) {
        if (array == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String arrayToString(Object[] array) {
        if (array == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}