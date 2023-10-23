public class Token {
    public String lexeme;
    public Type type;
    public Token(String lexeme, Type type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", lexeme, type);
    }
}
