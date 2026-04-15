package ru.vsu.chuprikov.task4;

public class BubbleSort {

    public static <T> void sort(T[] data, int[] value) {
        if (data.length != value.length) {
            throw new IllegalArgumentException("Число данных отличается от числа значений");
        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - i - 1;j++) {
                if (value[j] > value[j + 1]) {
                    T temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;

                    value[j] += value[j + 1];
                    value[j + 1] = value[j] - value[j + 1];
                    value[j] -= value[j + 1];
                }
            }
        }
    }
}
