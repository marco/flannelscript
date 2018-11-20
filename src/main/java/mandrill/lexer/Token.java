package mandrill.lexer;

public class Token {
    private TokenType tokenType;
    private Object value;

    Token(TokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value != null) {
            return "[" + tokenType.name() + " " + value + "]";
        }

        return "[" + tokenType.name() + "]";
    }
}