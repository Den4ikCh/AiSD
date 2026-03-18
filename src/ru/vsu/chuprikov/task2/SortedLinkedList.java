package ru.vsu.chuprikov.task2;

import java.util.Iterator;

public class SortedLinkedList<T> implements Iterable<T> {

    protected static class Node<T> {
        public T value;
        public Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(T value) {
            this(value, null);
        }

        public Node() {
            this(null, null);
        }
    }

    protected Node<T> head;
    protected Node<T> tail;
    protected int size;

    public SortedLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public SortedLinkedList(T value) {
        this();
        add(value);
    }

    public SortedLinkedList(T... values) {
        this();
        for (T value : values) {
            add(value);
        }
    }

    protected void checkEmpty() throws SortedLinkedListException {
        if (isEmpty()) {
            throw new SortedLinkedListException("Список пуст");
        }
    }

    public T getFirst() throws SortedLinkedListException {
        checkEmpty();
        return head.value;
    }

    public T getLast() throws SortedLinkedListException {
        checkEmpty();
        return tail.value;
    }

    protected Node<T> getItem(int index) throws SortedLinkedListException {
        if (index < 0 || index >= size()) {
            throw new SortedLinkedListException(String.format("Неверный индекс %d", index));
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    public T get(int index) throws SortedLinkedListException {
        return getItem(index).value;
    }

    public void addFirst(T value) {
        head = new Node<>(value, head);
        size++;
        if (size == 1) {
            tail = head;
        }
    }

    public void addLast(T value) {
        Node<T> temp = new Node<>(value);
        if (size == 0) {
            head = tail = temp;
        } else {
            tail = tail.next = temp;
        }
        size++;
    }

    public void add(T value) {
        Node<T> newTail = new Node<>(value);
        size++;
        if (size == 1) {
            head = tail = newTail;
        }
        else {
            tail = tail.next = newTail;
        }
    }

    public void insert(T value, int index) throws SortedLinkedListException {
        if (index == 0) {
            addFirst(value);
        } else {
            if (index < 0 || index >= size()) {
                throw new SortedLinkedListException(String.format("Неверный индекс %d", index));
            }
            Node<T> previous = getItem(index);
            Node<T> newNode = new Node<>(value, previous.next);
            previous.next = newNode;
            if (size == index) {
                tail = newNode;
            }
            size++;
        }
    }

    public T removeFirst() throws SortedLinkedListException {
        checkEmpty();
        T value = head.value;
        head = head.next;
        size--;
        if (size == 0) {
            tail = null;
        }
        return value;
    }

    public T removeLast() throws SortedLinkedListException {
        checkEmpty();
        if (size == 1) {
            return removeFirst();
        }
        tail = getItem(size - 2);
        T value = tail.next.value;
        tail.next = null;
        size--;
        return value;
    }

    public T remove(int index) throws SortedLinkedListException {
        if (index < 0 || index >= size()) {
            throw new SortedLinkedListException(String.format("Неверный индекс %d", index));
        }
        if (index == 0) {
            return removeFirst();
        }
        Node<T> previous = getItem(index - 1);
        T value = previous.value;
        previous.next = previous.next.next;
        size--;
        return value;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static SortedLinkedList<Number> combine(SortedLinkedList<Number> first, SortedLinkedList<Number> second) {
        SortedLinkedList<Number> result = new SortedLinkedList<>();
        result.size = first.size + second.size;

        if (first.isEmpty() && second.isEmpty()) {
            return result;
        }

        Node<Number> current = null;
        Node<Number> previous = null;

        while (!first.isEmpty() || !second.isEmpty()) {
            if (second.isEmpty()) {
                current = first.head;
                first.head = current.next;
                first.size--;
            } else if (first.isEmpty()) {
                current = second.head;
                second.head = current.next;
                second.size--;
            } else if (first.head.value.doubleValue() < second.head.value.doubleValue()) {
                current = first.head;
                first.head = current.next;
                first.size--;
            } else {
                current = second.head;
                second.head = current.next;
                second.size--;
            }

            current.next = null;

            if (result.head == null) {
                result.head = current;
            } else {
                previous.next = current;
            }
            previous = current;
        }
        result.tail = previous;

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T value = current.value;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}

