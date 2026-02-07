package ru.vsu.chuprikov.task1;

public class Main {
    public static void main(String[] args) {
        Formula formula = new Formula();
        try {
            formula.prepare("b-(c*(e-d)+c*(a-b))-a"); // 2-(3*(5-4)+3*(1-2))-1
            System.out.println(formula.execute(1, 2, 3, 4, 5));
            formula.prepare("x*(x +y)");
            System.out.println(formula.execute(2, 5.7));
            formula.prepare("(1)(2)aa");
            System.out.println(formula.execute(2));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
