package mandrill.lexer;

import java.util.regex.Pattern;

public enum TokenType {
    PUNCTUATOR_OPENING_PARENTHESIS("\\(", false),
    PUNCTUATOR_CLOSING_PARENTHESIS("\\)", false),
    PUNCTUATOR_OPENING_SQUARE("\\[", false),
    PUNCTUATOR_CLOSING_SQUARE("\\]", false),
    PUNCTUATOR_OPENING_CURLY("\\{", false),
    PUNCTUATOR_CLOSING_CURLY("\\}", false),
    PUNCTUATOR_OPENING_ANGLE("<", false),
    PUNCTUATOR_CLOSING_ANGLE(">", false),
    PUNCTUATOR_COMMA(",", false),
    PUNCTUATOR_SEMICOLON(";", false),
    PUNCTUATOR_COLON(":", false),

    OPERATOR_PLUS("\\+", false),
    OPERATOR_MINUS("\\-", false),
    OPERATOR_TIMES("\\*", false),
    OPERATOR_DIVIDE("\\/", false),
    OPERATOR_EXPONENTIAL("\\^", false),
    OPERATOR_MODULO("%", false),
    OPERATOR_EQUALS("=", false),
    OPERATOR_AMPERSAND("&", false),
    OPERATOR_BAR("\\|", false),

    LITERAL_STRING("'([^'\\\\]|\\\\.)*'", true),
    LITERAL_FLOAT("([0-9]+\\.[0-9]+)(?=[^0-9\\.])", true),
    LITERAL_INT("([0-9]+)(?=[^0-9\\.])", true),
    LITERAL_BOOLEAN("true(?=[^_A-Za-z])|false(?=[^_A-Za-z])", true),

    KEYWORD_IF("if(?=[^_A-Za-z])", false),
    KEYWORD_WHILE("while(?=[^_A-Za-z])", false),
    KEYWORD_ECHO("echo(?=[^_A-Za-z])", false),
    KEYWORD_RETURN("return(?=[^_A-Za-z])", false),

    IDENTIFIER_CAPITALIZED("[A-Z][_A-Za-z]*(?=[^_A-Za-z])", true),
    IDENTIFIER_LOWERCASED("[a-z][_A-Za-z]*(?=[^_A-Za-z])", true),
    IDENTIFIER_UNDERSCORE("_[_A-Za-z]*(?=[^_A-Za-z])", true),

    COMMENT("/\\*.*\\*/", false);

    private String regexString;
    private boolean shouldIncludeValue;

    TokenType(String regexString, boolean shouldIncludeValue) {
        this.regexString = regexString;
        this.shouldIncludeValue = shouldIncludeValue;
    }

    Pattern getRegexPattern() {
        return Pattern.compile(regexString);
    }

    boolean getShouldIncludeValue() {
        return shouldIncludeValue;
    }
}
