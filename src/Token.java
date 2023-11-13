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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) { return false; }

        Token token = (Token) obj;
        return token.lexeme.equals(this.lexeme) && token.type == this.type;
    }
}
