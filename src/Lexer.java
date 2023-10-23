public class Lexer {
    public int current = 0;

    public int next() {
        return current++;
    }
}
