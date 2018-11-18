package mandrill.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
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
                    Token newToken = new Token(
                        tokenTypes[j],
                        matcher.group()
                    );
                    currentTokenList.add(newToken);
                    currentTokenString = "";
                }
            }
        }

        return currentTokenList;
    }

    public static void lexAndPrint(String input) {
        System.out.println("Tokenizing: " + input));
        System.out.println(lex(input));
    }
}
