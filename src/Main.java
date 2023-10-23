import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Lexer lx = new Lexer();
    private static final ArrayList<Token> tokens = new ArrayList<>();
    private static String input;

    public static void main(String[] args) {
        System.out.print("Insira uma expressão matemática, lembre de adicionar espaços após números \nparênteses e operadores: ");
        input = scanner.nextLine();
        input += ' ';

        lex();
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }

    public static void lex() {
        int state = 0;
        StringBuilder currentLexeme = new StringBuilder();

        while (lx.current < input.length()) {
            char c = input.charAt(lx.current);
            char next = lx.current + 1 < input.length() ? input.charAt(lx.current + 1) : ' ';

            switch (state) {
                case 0 -> {
//                    if (isFinal()) {
//                        currentLexeme.append(c);
//                        tokens.add(new Token(currentLexeme.toString(), Type.INTEGER));
//                        lx.current++;
//                    } else
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
                        lx.current++;
                        currentLexeme.append(c);
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
                        lx.current++;
                    } else {
                        state = 0;
                        tokens.add(new Token("ERROR", Type.ERROR));
                    }
                }
                case 4 -> {
                    if (isDigit(c)) {
                        currentLexeme.append(c);
                        if (isEmptySpace(next)) {
                            state = 5;
                        }
                        lx.current++;
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
        return (c == ' '|| c == '\t' || c == '\n' || c == '\r');
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

    public static boolean isFinal() {
        return lx.current == input.length() - 1;
    }
}