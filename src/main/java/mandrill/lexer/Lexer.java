package mandrill.lexer;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static void main(String[] args) {
        try {
            byte[] inputBytes = Files.readAllBytes(Paths.get(args[0]));
            String input = new String(inputBytes, Charset.forName("UTF-8"));
            lexAndOutput(input);
        } catch (Exception exception) {
            System.out.println(exception);
            System.exit(1);
        }
    }

    public static List<Token> lex(String input) {
        TokenType[] tokenTypes = TokenType.values();
        List<Token> currentTokenList = new ArrayList<Token>();
        String currentTokenString = "";

        for (int i = 0; i < input.length(); i++) {
            currentTokenString += input.charAt(i);
            for (int j = 0; j < tokenTypes.length; j++) {
                Pattern pattern = tokenTypes[j].getRegexPattern();
                Matcher matcher = pattern.matcher(currentTokenString);

                if (matcher.find()) {
                    Object value;

                    if (tokenTypes[j].getShouldIncludeValue()) {
                        value = matcher.group();
                    } else {
                        value = null;
                    }

                    Token newToken = new Token(
                        tokenTypes[j],
                        value
                    );
                    currentTokenList.add(newToken);
                    currentTokenString = currentTokenString.substring(matcher.end());
                }
            }
        }

        return currentTokenList;
    }

    public static void lexAndPrint(String input) {
        System.out.println("Tokenizing: " + input);
        System.out.println(lex(input));
    }

    public static void lexAndOutput(String input) {
        List<Token> result = lex(input);
        StringBuilder stringBuilder = new StringBuilder();

        for (Token token : result) {
            stringBuilder.append(token.toString() + "\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
