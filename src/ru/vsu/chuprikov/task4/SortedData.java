package ru.vsu.chuprikov.task4;

public class SortedData<T> {
    public final T[] data;
    public final int[] orderValues;

    public SortedData(T[] data, int[] orderValues) {
        this.data = data;
        this.orderValues = orderValues;
    }
}
