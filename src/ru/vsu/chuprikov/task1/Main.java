package ru.vsu.chuprikov.task1;

public class Main {
    public static void main(String[] args) {
        Formula formula = new Formula();
        try {
            System.out.println("=== Пример 1 ===");
            Formula f1 = new Formula();
            f1.prepare("x-y(z+2/(x+y*y))");
            System.out.println("Формула после prepare: " + f1.getFormula());
            System.out.println("Переменные: " + f1.getVariables());
            System.out.println("Результат: " + f1.execute(1, 2, 3));

            System.out.println("\n=== Пример 2 ===");
            Formula f2 = new Formula();
            f2.prepare("a(b+c(d-e(f+g)))");
            System.out.println("Формула после prepare: " + f2.getFormula());
            System.out.println("Переменные: " + f2.getVariables());
            System.out.println("Результат: " + f2.execute(2, 3, 4, 5, 6, 7, 8));

            System.out.println("\n=== Пример 3 ===");
            Formula f3 = new Formula();
            f3.prepare("x/y+z*a-b/c");
            System.out.println("Формула после prepare: " + f3.getFormula());
            System.out.println("Переменные: " + f3.getVariables());
            System.out.println("Результат: " + f3.execute(4, 20, 5, 10, 2, 3));

            System.out.println("\n=== Пример 4 ===");
            Formula f4 = new Formula();
            f4.prepare("x*-y+z/-a");
            System.out.println("Формула после prepare: " + f4.getFormula());
            System.out.println("Переменные: " + f4.getVariables());
            System.out.println("Результат: " + f4.execute(2, 5, 2, 10));

            System.out.println("\n=== Пример 5 ===");
            Formula f5 = new Formula();
            f5.prepare("(a+b)(c-d)/(e+f)(g-h)");
            System.out.println("Формула после prepare: " + f5.getFormula());
            System.out.println("Переменные: " + f5.getVariables());
            System.out.println("Результат: " + f5.execute(1, 2, 3, 1, 4, 2, 5, 1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
