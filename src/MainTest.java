import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void lex() {
        String input = "1 + 2 * 3";
        ArrayList<Token> expectedTokens = new ArrayList<>(
                Arrays.asList(
                        new Token("1", Type.INTEGER),
                        new Token("+", Type.ADDITION),
                        new Token("2", Type.INTEGER),
                        new Token("*", Type.MULTIPLICATION),
                        new Token("3", Type.INTEGER)
                )
        );
        ArrayList<Token> actual = Main.lex(input);
        assertEquals(expectedTokens, actual);
    }

    @Test
    void infix() {
        String input = "1 + 2 * 3";
        ArrayList<Token> tokens = Main.lex(input);
        String expected = "7.0";
        String actual = Main.infix(tokens);

        assertEquals(expected, actual);
    }

    @Test
    void postfix() {
        String input = "1 2 3 * +";
        ArrayList<Token> tokens = Main.lex(input);

        String expected = "7.0";
        String actual = Main.postfix(tokens);

        assertEquals(expected, actual);
    }
}