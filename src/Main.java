import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Lexer lx = new Lexer();
//    private static final ArrayList<Token> tokens = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            int res;
            String input;

            System.out.println("1 - Infixa");
            System.out.println("2 - Posfixa");
            System.out.println("3 - Sair");

            System.out.print("Escolha uma opção: ");
            res = scanner.nextInt();

            switch (res) {
                case 1: {
                    System.out.print(
                            "Insira uma expressão matemática, lembre de adicionar espaços após números" +
                                    " \nparênteses e operadores: "
                    );
                    input = scanner.nextLine();

                    ArrayList<Token> tokens = lex(input);

                    infix(tokens);
                    break;
                }
                case 2: {
                    System.out.print(
                            "Insira uma expressão matemática, lembre de adicionar espaços após números" +
                                    " \nparênteses e operadores: "
                    );
                    input = scanner.nextLine();

                    ArrayList<Token> tokens = lex(input);

                    postfix(tokens);
                    break;
                }
                case 3: {
                    System.exit(0);
                }
            }
        }
    }

    public static String postfix(ArrayList<Token> tokens) {
        ArrayList<String> postfix = new ArrayList<>();

        for (Token token : tokens) {
            postfix.add(token.lexeme);

            if (
                    token.type == Type.ADDITION ||
                            token.type == Type.SUBTRACTION ||
                            token.type == Type.MULTIPLICATION ||
                            token.type == Type.DIVISION
            ) {
                double firstOperand = Double.parseDouble(postfix.get(postfix.size() - 3));
                double secondOperand = Double.parseDouble(postfix.get(postfix.size() - 2));
                String operator = postfix.get(postfix.size() - 1);

                System.out.println(firstOperand + " " + operator + " " + secondOperand);

                postfix.subList(postfix.size() - 3, postfix.size()).clear();

                doOperation(postfix, firstOperand, secondOperand, operator);
            }
        }

        System.out.println("Postfix: " + postfix);
        return postfix.get(0);
    }

    public static String infix(ArrayList<Token> tokens) {
        ArrayList<String> infix = new ArrayList<>();

        for (Token token : tokens) {
            infix.add(token.lexeme);

            if (token.type == Type.CLOSE_PAREN) {
                double firstOperand = Double.parseDouble(infix.get(infix.size() - 4));
                double secondOperand = Double.parseDouble(infix.get(infix.size() - 2));
                String operator = infix.get(infix.size() - 3);

                infix.subList(infix.size() - 5, infix.size()).clear();

                doOperation(infix, firstOperand, secondOperand, operator);
            }
        }

        while (infix.size() > 1) {
            int opIndex;

            if (infix.contains("/")) {
                opIndex = infix.indexOf("/");
                double firstOperand = Double.parseDouble(infix.get(opIndex - 1));
                double secondOperand = Double.parseDouble(infix.get(opIndex + 1));

                infix.subList(opIndex - 1, opIndex + 2).clear();

                infix.add(opIndex - 1, String.valueOf(firstOperand / secondOperand));
                continue;
            }

            if (infix.contains("*")) {
                opIndex = infix.indexOf("*");

                double firstOperand = Double.parseDouble(infix.get(opIndex - 1));
                double secondOperand = Double.parseDouble(infix.get(opIndex + 1));

                infix.subList(opIndex - 1, opIndex + 2).clear();

                infix.add(opIndex - 1, String.valueOf(firstOperand * secondOperand));
                continue;
            }

            if (infix.contains("+")) {
                opIndex = infix.indexOf("+");

                double firstOperand = Double.parseDouble(infix.get(opIndex - 1));
                double secondOperand = Double.parseDouble(infix.get(opIndex + 1));

                infix.subList(opIndex - 1, opIndex + 2).clear();

                infix.add(opIndex - 1, String.valueOf(firstOperand + secondOperand));
                continue;
            }

            if (infix.contains("-")) {
                opIndex = infix.indexOf("-");

                double firstOperand = Double.parseDouble(infix.get(opIndex - 1));
                double secondOperand = Double.parseDouble(infix.get(opIndex + 1));

                infix.subList(opIndex - 1, opIndex + 2).clear();

                infix.add(opIndex - 1, String.valueOf(firstOperand - secondOperand));
            }
        }

        System.out.println("Infix: " + infix);
        return infix.get(0);
    }

    private static void doOperation(
            ArrayList<String> array, double firstOperand, double secondOperand, String operator
    ) {
        switch (operator) {
            case "+" -> array.add(String.valueOf(firstOperand + secondOperand));
            case "-" -> array.add(String.valueOf(firstOperand - secondOperand));
            case "*" -> array.add(String.valueOf(firstOperand * secondOperand));
            case "/" -> array.add(String.valueOf(firstOperand / secondOperand));
        }
    }

    public static ArrayList<Token> lex(String input) {
        int state = 0;
        ArrayList<Token> tokens = new ArrayList<>();
        StringBuilder currentLexeme = new StringBuilder();

        while (lx.current < input.length()) {
            char c = input.charAt(lx.current);
            char next = lx.current + 1 < input.length() ? input.charAt(lx.current + 1) : ' ';

            switch (state) {
                case 0 -> {
                    if (isOpenParenthesis(c)) {
                        currentLexeme.append(c);
                        state = 15;
                    } else if (isCloseParenthesis(c)) {
                        currentLexeme.append(c);
                        state = 17;
                    } else if (isDigit(c)) {
                        state = 1;
                        currentLexeme.append(c);
                    } else if (isOperator(c)) {
                        state = 6;
                        currentLexeme.append(c);
                    } else if (isEmptySpace(c)) {
                        lx.current++;
                    } else {
                        lx.current++;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 1 -> {
                    if (isDigit(c)) {
                        if (isEmptySpace(next) || lx.current == input.length() - 1) {
                            state = 2;
                            break;
                        }
                        if (isDecimalPoint(next)) {
                            lx.current++;
                            break;
                        }
                        lx.current++;
                        currentLexeme.append(input.charAt(lx.current));
                    } else if (isDecimalPoint(c)) {
                        currentLexeme.append(c);
                        state = 3;
                        lx.current++;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 2 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.INTEGER));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 3 -> {
                    if (isDigit(c)) {
                        currentLexeme.append(c);
                        state = 4;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 4 -> {
                    if (isDigit(c)) {
                        if (isEmptySpace(next) || input.charAt(lx.current + 1) == ' ') {
                            state = 5;
                            break;
                        }
                        lx.current++;
                        currentLexeme.append(c);
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 5 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.REAL));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 6 -> {
                    if (isAddition(c)) {
                        state = 7;
                    } else if (isSubtraction(c)) {
                        state = 9;
                    } else if (isDivision(c)) {
                        state = 11;
                    } else if (isMultiplication(c)) {
                        state = 13;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 7 -> {
                    if (isEmptySpace(next)) {
                        state = 8;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 8 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.ADDITION));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 9 -> {
                    if (isEmptySpace(next)) {
                        state = 10;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 10 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.SUBTRACTION));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 11 -> {
                    if (isEmptySpace(next)) {
                        state = 12;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 12 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.DIVISION));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 13 -> {
                    if (isEmptySpace(next)) {
                        state = 14;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 14 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.MULTIPLICATION));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 15 -> {
                    if (isEmptySpace(next)) {
                        state = 16;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 16 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.OPEN_PAREN));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
                case 17 -> {
                    if (isEmptySpace(next)) {
                        state = 18;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 18 -> {
                    tokens.add(new Token(currentLexeme.toString(), Type.CLOSE_PAREN));
                    state = 0;
                    currentLexeme = new StringBuilder();
                    lx.current++;
                }
            }
        }
        return tokens;
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isOperator(char c) {
        return isAddition(c) || isSubtraction(c) || isMultiplication(c) || isDivision(c);
    }

    public static boolean isAddition(char c) {
        return c == '+';
    }

    public static boolean isSubtraction(char c) {
        return c == '-';
    }

    public static boolean isMultiplication(char c) {
        return c == '*';
    }

    public static boolean isDivision(char c) {
        return c == '/';
    }

    public static boolean isEmptySpace(char c) {
        return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
    }

    public static boolean isDecimalPoint(char c) {
        return c == '.';
    }

    public static boolean isOpenParenthesis(char c) {
        return c == '(';
    }

    public static boolean isCloseParenthesis(char c) {
        return c == ')';
    }
}