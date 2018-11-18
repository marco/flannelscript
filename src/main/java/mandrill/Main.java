package mandrill;

import mandrill.lexer.Lexer;

public class Main {
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("ERROR: No argument provided.");
            return;
        }

        Lexer.lexAndPrint(args[0]);
    }
}
