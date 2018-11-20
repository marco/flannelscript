package mandrill.parser;

import mandrill.parser.generatednodes.*;

/**
 * An abstract syntax tree node that extends from JavaCC's `SimpleNode` class.
 */
public class ASTNode extends SimpleNode {
    public ASTNode(int id) {
        super(id);
    }

    public ASTNode(Parser parser, int id) {
        super(parser, id);
    }

    /**
     * Returns a string representation of this node. If this node has a
     * `value`, the result will be in the form `type:value`, otherwise it will
     * be in the form `type`.
     *
     * @return The string representation.
     */
    public String toString() {
        if (value != null) {
            return super.toString() + ":" + this.value;
        }

        return super.toString();
    }

    /**
     * Sets this token's `value` to part of the image for a `Token`. E.g., if
     * the token is `[LITERAL_INT 13]`, `13` will be used.
     *
     * @param token The token to use.
     */
    public void setFilteredValueForToken(Token token) {
        String value = token.image;

        this.value = value.substring(
            value.indexOf(' ') + 1,
            value.length() - 1
        );
    }
}
