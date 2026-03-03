package ru.vsu.chuprikov.task2;

import ru.vsu.chuprikov.utils.ConsoleUtils;
import ru.vsu.chuprikov.utils.InputArgs;

import java.io.FileNotFoundException;
import java.util.List;

class ConsoleApp {
    public static void main(String[] args) {
        InputArgs inputArgs = ConsoleUtils.parseCmdArgs(args);
        String path = System.getProperty("user.dir") + "\\src\\ru\\vsu\\chuprikov\\task2\\";
        try {
            SortedLinkedList<Number>[] lists = ConsoleUtils.readTwoListsFromFile(path + inputArgs.inputFile);
            SortedLinkedList<Number> result = SortedLinkedList.combine(lists[0], lists[1]);
            ConsoleUtils.printList(path + inputArgs.outputFile, result);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("File %s.", e.getMessage()));
        }
    }
}
