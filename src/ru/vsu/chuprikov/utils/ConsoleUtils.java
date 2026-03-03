package ru.vsu.chuprikov.utils;

import ru.vsu.chuprikov.task2.SortedLinkedList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUtils {
    public static double getDouble(String request) {
        Locale.setDefault(Locale.US);

        double result;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(request);
            if (in.hasNextDouble()) {
                result = in.nextDouble();
                break;
            }
            else {
                System.out.println("Вы неверно ввели данные, попробуйте ещё раз.");
                in.nextLine();
            }
        }
        return result;
    }

    public static double getPositiveDouble(String request) {
        Locale.setDefault(Locale.US);

        double result;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(request);
            if (in.hasNextDouble()) {
                result = in.nextDouble();
                if (result <= 0) {
                    System.out.println("Введённое число не является положительным.");
                    in.nextLine();
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Вы неверно ввели данные, попробуйте ещё раз.");
                in.nextLine();
            }
        }
        return result;
    }

    public static int getInt(String request) {
        Locale.setDefault(Locale.US);

        int result;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(request);
            if (in.hasNextInt()) {
                result = in.nextInt();
                break;
            }
            else {
                System.out.println("Вы неверно ввели данные, попробуйте ещё раз.");
                in.nextLine();
            }
        }
        return result;
    }

    public static int getIntMoreThan(String request, int n) {
        Locale.setDefault(Locale.US);

        int result;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(request);
            if (in.hasNextInt()) {
                result = in.nextInt();
                if (result <= n) {
                    System.out.printf("Необходимо ввести число >%d.\n", n);
                    in.nextLine();
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Вы неверно ввели данные, попробуйте ещё раз.");
                in.nextLine();
            }
        }
        return result;
    }

    public static int getPositiveInt(String request) {
        Locale.setDefault(Locale.US);

        int result;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(request);
            if (in.hasNextInt()) {
                result = in.nextInt();
                if (result <= 0) {
                    System.out.println("Введённое число не является положительным.");
                    in.nextLine();
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Вы неверно ввели данные, попробуйте ещё раз.");
                in.nextLine();
            }
        }
        return result;
    }

    public static long getPositiveLong(String request) {
        Locale.setDefault(Locale.US);

        long result;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(request);
            if (in.hasNextLong()) {
                result = in.nextLong();
                if (result <= 0) {
                    System.out.println("Введённое число не является положительным.");
                    in.nextLine();
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Вы неверно ввели данные, попробуйте ещё раз.");
                in.nextLine();
            }
        }
        return result;
    }

    public static SortedLinkedList<Number>[] readTwoListsFromFile(String fileNameWithPath) throws FileNotFoundException {
        SortedLinkedList<Number> list1 = new SortedLinkedList<>();
        SortedLinkedList<Number> list2 = new SortedLinkedList<>();
        File file = new File(fileNameWithPath);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileNameWithPath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null && lineNumber < 2) {
                String[] tokens = line.trim().split("[,\\s]+");
                SortedLinkedList<Number> currentList = (lineNumber == 0) ? list1 : list2;

                for (String token : tokens) {
                    if (token.isEmpty()) continue;

                    try {
                        currentList.add(Integer.parseInt(token));
                    } catch (NumberFormatException e1) {
                        try {
                            currentList.add(Long.parseLong(token));
                        } catch (NumberFormatException e2) {
                            try {
                                currentList.add(Double.parseDouble(token));
                            } catch (NumberFormatException e3) {
                                System.out.println("Не удалось распарсить число: " + token);
                            }
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return new SortedLinkedList[]{list1, list2};
    }

    public static void printList(String fileNameWithPath, SortedLinkedList<Number> list) throws FileNotFoundException {
        File file = new File(fileNameWithPath);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            for (int i = 0; i < list.size(); i++) {
                fileWriter.write(list.get(i).toString());
                if (i < list.size() - 1) {
                    fileWriter.write(" ");
                }
            }
            fileWriter.write("\n");
        } catch (Exception e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static InputArgs parseCmdArgs(String[] args) {
        String input = null;
        String output = null;
        for (String arg : args) {
            switch (arg) {
                case "-help":
                case "-h":
                case "--help":
                    printHelp();
                    System.exit(0);
                    break;
                case "-i":
                case "-input":
                case "--input-file":
                    input = "";
                    break;
                case "-o":
                case "-output":
                case "--output-file":
                    output = "";
                    break;
                default:
                    if (input == "") {
                        input = arg;
                    } else if (output == "") {
                        output = arg;
                    }
            }
            if (arg.startsWith("--input-file=")) {
                input = arg.substring("--input-file=".length());
            } else if (arg.startsWith("--output-file=")) {
                output = arg.substring("--output-file=".length());
            }
        }
        if (input == null || output == null || input == "" || output == "") {
            System.err.println("Необходимо ввести названия входных и выходных файлов.");
            System.exit(1);
        } else if (!input.endsWith(".txt") || !output.endsWith(".txt")) {
            System.err.println("Поддерживаются только текстовые файлы расширения .txt.");
            System.exit(2);
        }
        return new InputArgs(input, output);
    }

    private static void printHelp() {
        System.out.println("Параметры:");
        System.out.println("Показать список параметров: -h, -help, --help");
        System.out.println("Название входного файла: -i, -input, --input-file, --input-file=");
        System.out.println("Название выходного файла: -o, -output, --output-file, --output-file=");
    }
}
