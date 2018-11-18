package mandrill.lexer;

import java.util.regex.Pattern;

class Token {
    private TokenType tokenType;
    private Object value;

    Token(TokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    TokenType getTokenType() {
        return tokenType;
    }

    Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return tokenType.name() + ": " + value;
    }
}

enum TokenType {
    PUNCTUATOR("[()\\[\\]{},;]"),
    OPERATOR ("\\+|\\-|\\*|\\/|\\^|%|=|\\+\\+|\\-\\-|\\+=|\\-=|\\*=|\\/=|\\^=|%="),
    STRING_LITERAL("'([^'\\\\]|\\\\.)*'"),
    NUMBER_LITERAL("([0-9]+(\\.[0-9]+)*)(?=[^0-9\\.])"),
    IDENTIFIER("[_A-Za-z]+(?=[^_A-Za-z])"),
    BOOLEAN_LITERAL("true(?=[^_A-Za-z])|false(?=[^_A-Za-z])"),
    COMMENT("\\/\\/.*");

    private String regexString;

    TokenType(String regexString) {
        this.regexString = regexString;
    }

    Pattern getRegexPattern() {
        return Pattern.compile(regexString);
    }
}
