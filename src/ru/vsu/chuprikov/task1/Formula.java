package ru.vsu.chuprikov.task1;

import java.util.*;

public class Formula {
    private String formula;
    private List<Character> variables;

    public Formula() {
        this.formula = "";
        this.variables = new ArrayList<>();
    }

    private void parseFormula(String formula) {
        variables.clear();
        for (char c : formula.toCharArray()) {
            if (isVariable(c) && !variables.contains(c)) {
                variables.add(c);
            }
        }
    }

    private double calculate(Map<Character, Double> valuesMap) {
        for (Character key : valuesMap.keySet()) {
            formula = formula.replaceAll(String.valueOf(key), String.valueOf(valuesMap.get(key)));
        }

        while (formula.contains("(")) {
            int openBracket = -1;
            int closeBracket = -1;

            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') {
                    openBracket = i;
                } else if (formula.charAt(i) == ')') {
                    if (openBracket != -1) {
                        closeBracket = i;
                        break;
                    }
                }
            }

            if (openBracket == -1 || closeBracket == -1) {
                throw new RuntimeException("Непарные скобки");
            }

            String insideBrackets = formula.substring(openBracket + 1, closeBracket);
            double bracketResult = calculate(insideBrackets);

            String before = formula.substring(0, openBracket);
            String after = formula.substring(closeBracket + 1);
            formula = before + bracketResult + after;
        }

        return calculate(formula);
    }

    private double calculate(String formula) {
        while (formula.contains("*") || formula.contains("/")) {
            int index = formula.indexOf("*");
            if (!formula.contains("*") || formula.contains("/") && formula.indexOf("/") < index) {
                index = formula.indexOf("/");
            }
            int leftIndex = index - 1;
            int rightIndex = index + 1;
            while (leftIndex >= 0 && isNumber(formula.charAt(leftIndex)) && formula.charAt(leftIndex) != '-') {
                leftIndex--;
            }
            leftIndex++;
            while (rightIndex < formula.length() && isNumber(formula.charAt(rightIndex))) {
                rightIndex++;
            }
            double left = Double.parseDouble(formula.substring(leftIndex, index));
            double right = Double.parseDouble(formula.substring(index + 1, rightIndex));
            double result = formula.contains("*") ? left * right : left / right;
            formula = formula.replace(formula.substring(leftIndex, rightIndex), String.valueOf(result));
        }

        formula = formula.replace("+-", "-");
        formula = formula.replace("--", "+");
        String[] parts = formula.split("(?=[+-])");
        double result = 0;

        for (String part : parts) {
            if (!part.isEmpty()) {
                result += Double.parseDouble(part);
            }
        }

        return result;
    }

    private boolean isNumber(char character) {
        return character >= '0' && character <= '9' || character == '.' || character == '-';
    }

    private boolean isVariable(char character) {
        return character >= 'a' && character <= 'z';
    }

    private boolean isOperator(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }

    public void prepare(String formula) throws Exception {
        formula = formula.replaceAll("\\s+", "");
        formula = formula.toLowerCase();

        for (char c : formula.toCharArray()) {
            if (!isNumber(c) && !isVariable(c) && !isOperator(c) && c != '(' && c != ')') {
                throw new Exception("Недопустимый символ '" + c + "' в формуле");
            }
        }

        if (formula.isEmpty()) {
            throw new Exception("Пустая формула");
        }

        for (int i = 0; i < formula.length() - 1; i++) {
            char current = formula.charAt(i);
            char next = formula.charAt(i + 1);

            if (isOperator(current) && isOperator(next)) {
                if (!((current == '*' || current == '/') && next == '-')) {
                    throw new Exception("Два оператора подряд в формуле: " + current + next);
                }
            }
        }

        char firstChar = formula.charAt(0);
        if (isOperator(firstChar) && firstChar != '-') {
            throw new Exception("Формула не может начинаться с оператора " + firstChar);
        }

        char lastChar = formula.charAt(formula.length() - 1);
        if (isOperator(lastChar)) {
            throw new Exception("Формула не может заканчиваться оператором");
        }

        int bracketCount = 0;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(') {
                bracketCount++;
                if (i < formula.length() - 1) {
                    char next = formula.charAt(i + 1);
                    if (isOperator(next) && next != '-') {
                        throw new Exception("После открывающей скобки не может идти оператор " + next);
                    }
                    if (next == ')') {
                        throw new Exception("Пустые скобки не допускаются");
                    }
                }
            } else if (c == ')') {
                bracketCount--;
                if (i > 0) {
                    char prev = formula.charAt(i - 1);
                    if (isOperator(prev)) {
                        throw new Exception("Перед закрывающей скобкой не может идти оператор");
                    }
                }
            }
        }
        if (bracketCount != 0) {
            throw new Exception("Непарные скобки");
        }

        StringBuilder result = new StringBuilder(formula);

        for (int i = result.length() - 2; i >= 0; i--) {
            char current = formula.charAt(i);
            char next = formula.charAt(i + 1);

            if ((isNumber(current) && current != '-' || isVariable(current)) && next == '(') {
                result.insert(i + 1, '*');
            }

            if (current == ')' && (isNumber(next) && next != '-' || isVariable(next))) {
                result.insert(i + 1, '*');
            }

            if (current == ')' && next == '(') {
                result.insert(i + 1, '*');
            }

            if (isVariable(current) && isVariable(next)) {
                result.insert(i + 1, '*');
            }

            if (current == '.' && !isNumber(next)) {
                throw new Exception("Некорректное использование точки в формуле");
            }
        }

        this.formula = result.toString();
        parseFormula(this.formula);
    }

    public double execute(double... values) throws Exception {
        if (values.length != variables.size()) {
            throw new IllegalArgumentException(
                    String.format("Ожидалось %d переменных, а получено %d", variables.size(), values.length)
            );
        }

        if (formula.isEmpty()) {
            throw new Exception("Отсутствует формула");
        }

        Collections.sort(variables);
        Map<Character, Double> valuesMap = new HashMap<>();
        for (int i = 0; i < variables.size(); i++) {
            valuesMap.put(variables.get(i), values[i]);
        }

        return calculate(valuesMap);
    }
}
