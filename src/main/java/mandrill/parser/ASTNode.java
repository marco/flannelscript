package mandrill.parser;

import java.util.Arrays;

import mandrill.parser.generatednodes.Node;
import mandrill.parser.generatednodes.SimpleNode;

/**
 * An abstract syntax tree node that extends from JavaCC's `SimpleNode` class.
 */
public class ASTNode extends SimpleNode implements Node {
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
    @Override
    public String toString() {
        if (value != null) {
            return getName() + ":" + getValue().toString();
        }

        return getName();
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

    /**
     * Returns this node's name.
     *
     * @return This node's name.
     */
    public String getName() {
        return super.toString();
    }

    /**
     * Returns this node's value.
     *
     * @return This node's value
     */
    public String getValue() {
        return (String) value;
    }

    /**
     * Returns this node's children.
     *
     * @return This node's children.
     */
    public ASTNode[] getChildren() {
        if (children == null) {
            return new ASTNode[] {};
        }

        return Arrays.copyOf(children, children.length, ASTNode[].class);
    }

    /**
     * Returns this node's child node at a specific index.
     *
     * @return The child node.
     */
    public ASTNode getChild(int index) {
        return getChildren()[index];
    }

    /**
     * Prints this node's name and value as well as its childen's names and
     * values.
     *
     * @param linePrefix The prefix to place before each line.
     * @param indent The amount of indentation for each nested node level.
     * @param itemPrefix The prefix to place before each item.
     */
    public void printNodeAndChildren(
        String linePrefix,
        String indent,
        String itemPrefix
    ) {
        System.out.println(linePrefix + itemPrefix + toString());

        if (children == null) {
            return;
        }

        for (int i = 0; i < children.length; i++) {
            ASTNode childNode = (ASTNode) children[i];

            if (childNode == null) {
                continue;
            }

            childNode.printNodeAndChildren(linePrefix + indent, indent, itemPrefix);
        }
    }

    @Override
    public void dump(String prefix) {
        printNodeAndChildren(prefix, "  ", "- ");
    }
}
