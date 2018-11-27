package mandrill.lexer;

import java.util.regex.Pattern;

public enum TokenType {
    PUNCTUATOR_OPENING_PARENTHESIS("(?<=^\\s*)\\(", false),
    PUNCTUATOR_CLOSING_PARENTHESIS("(?<=^\\s*)\\)", false),
    PUNCTUATOR_OPENING_SQUARE("(?<=^\\s*)\\[", false),
    PUNCTUATOR_CLOSING_SQUARE("(?<=^\\s*)\\]", false),
    PUNCTUATOR_OPENING_CURLY("(?<=^\\s*)\\{", false),
    PUNCTUATOR_CLOSING_CURLY("(?<=^\\s*)\\}", false),
    PUNCTUATOR_OPENING_ANGLE("(?<=^\\s*)<", false),
    PUNCTUATOR_CLOSING_ANGLE("(?<=^\\s*)>", false),
    PUNCTUATOR_COMMA("(?<=^\\s*),", false),
    PUNCTUATOR_PERIOD("(?<=^\\s*)\\.", false),
    PUNCTUATOR_SEMICOLON("(?<=^\\s*);", false),
    PUNCTUATOR_COLON("(?<=^\\s*):", false),
    PUNCTUATOR_EXCLAMATION("(?<=^\\s*)!", false),

    OPERATOR_PLUS("(?<=^\\s*)\\+", false),
    OPERATOR_MINUS("(?<=^\\s*)\\-", false),
    OPERATOR_TIMES("(?<=^\\s*)\\*", false),
    OPERATOR_DIVIDE("(?<=^\\s*)\\/", false),
    OPERATOR_EXPONENTIAL("(?<=^\\s*)\\^", false),
    OPERATOR_MODULO("(?<=^\\s*)%", false),
    OPERATOR_EQUALS("(?<=^\\s*)=", false),
    OPERATOR_AMPERSAND("(?<=^\\s*)&", false),
    OPERATOR_BAR("(?<=^\\s*)\\|", false),

    LITERAL_STRING("(?<=^\\s*)'([^'\\\\]|\\\\.)*'", true),
    LITERAL_FLOAT("(?<=^\\s*)([0-9]+\\.[0-9]+)(?=[^0-9\\.])", true),
    LITERAL_INT("(?<=^\\s*)([0-9]+)(?=[^0-9\\.])", true),
    LITERAL_BOOLEAN("(?<=^\\s*)true(?=[^_A-Za-z])|false(?=[^_A-Za-z])", true),

    KEYWORD_IF("(?<=^\\s*)if(?=[^_A-Za-z])", false),
    KEYWORD_WHILE("(?<=^\\s*)while(?=[^_A-Za-z])", false),
    KEYWORD_ECHO("(?<=^\\s*)echo(?=[^_A-Za-z])", false),
    KEYWORD_RETURN("(?<=^\\s*)return(?=[^_A-Za-z])", false),

    IDENTIFIER_CAPITALIZED("(?<=^\\s*)[A-Z][_A-Za-z]*(?=[^_A-Za-z])", true),
    IDENTIFIER_LOWERCASED("(?<=^\\s*)[a-z][_A-Za-z]*(?=[^_A-Za-z])", true),
    IDENTIFIER_UNDERSCORE("(?<=^\\s*)_[_A-Za-z]*(?=[^_A-Za-z])", true),

    COMMENT("\\s*/\\*.*\\*/", false);

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
