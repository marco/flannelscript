package mandrill.runtime;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import mandrill.parser.ASTNode;
import mandrill.parser.generatednodes.*;

public class RuntimeNode {
    public static void runRootNode(ASTNode node) {
        RuntimeConstants.setGlobals();
        RuntimeNode.runASTNodes(node.getChildren(), new RuntimeContext(null), false);
    }

    public static void runASTNode(ASTNode node, RuntimeContext context, boolean isInFunction) {
        if (node instanceof ASTGenerated_inside_function_action) {
            runASTNode(node.getChild(0), context, isInFunction);
            return;
        }

        if (node instanceof ASTGenerated_statement_call) {
            runASTNode(node.getChild(0), context, isInFunction);
            return;
        }

        if (node instanceof ASTGenerated_variable_declaration) {
            String typeName
                = node.getChild(0).getChild(0).getValue();
            String variableName
                = node.getChild(1).getChild(0).getValue();
            CreatedObject evaluatedValue
                = evaluateASTNode(node.getChild(2), context);

            if (
                RuntimeContext.getClass(typeName)
                    != evaluatedValue.getObjectClass()
            ) {
                throw new RuntimeNodeException(
                    "Type `" + typeName + "` does not match found type."
                );
            }

            context.setLocal(variableName, evaluatedValue);
        }

        if (node instanceof ASTGenerated_variable_assignment) {
            String variableName
                = node.getChild(0).getChild(0).getValue();
            CreatedObject evaluatedValue
                = evaluateASTNode(node.getChild(1), context);

            context.updateObject(variableName, evaluatedValue);
        }

        if (node instanceof ASTGenerated_function_call) {
            String functionName
                = node.getChild(0).getChild(0).getValue();
            ASTNode[] values = node.getChild(1).getChildren();
            List<CreatedObject> arguments = new LinkedList<CreatedObject>();

            for (int i = 0; i < values.length; i++) {
                arguments.add(evaluateASTNode(values[i], context));
            }

            CreatedFunction function = context.getFunction(functionName);
            function.call(
                context.getOpenObject(),
                arguments.toArray(new CreatedObject[0])
            );
        }

        if (node instanceof ASTGenerated_echo_statement) {
            CreatedObject evaluatedObject = evaluateASTNode(
                node.getChild(0),
                context
            );

            if (
                evaluatedObject.getObjectClass()
                    == RuntimeContext.getClass("Str")
            ) {
                System.out.println(evaluatedObject.getBaseValue());
                return;
            }

            System.out.println(
                evaluatedObject
                    .callMethod("getStr", new CreatedObject[] {})
                    .getBaseValue()
            );
            return;
        }

        if (node instanceof ASTGenerated_return_statement) {
            System.out.println(
                "returned: "
                    + evaluateASTNode(node.getChild(0), context)
            );
            System.exit(0);
        }

        if (node instanceof ASTGenerated_while_statement) {
            if (
                evaluateASTNode(node.getChild(0), context).getObjectClass()
                    != RuntimeConstants.getBlnClass()
            ) {
                throw new RuntimeNodeException("Expected `Bln` type, but was not found.");
            }

            while ((Boolean) evaluateASTNode(node.getChild(0), context).getBaseValue() == true) {
                runASTNodes(node.getChild(1).getChildren(), context, isInFunction);
            }
        }

        if (node instanceof ASTGenerated_if_statement) {
            if (
                evaluateASTNode(node.getChild(0), context).getObjectClass()
                    != RuntimeConstants.getBlnClass()
            ) {
                throw new RuntimeNodeException("Expected `Bln` type, but was not found.");
            }

            if ((Boolean) evaluateASTNode(node.getChild(0), context).getBaseValue() == true) {
                runASTNodes(node.getChild(1).getChildren(), context, isInFunction);
            }
        }

        if (node instanceof ASTGenerated_class_declaration) {
            ASTNode[] children = node.getChildren();
            String className = children[0].getValue();

            DefaultPropertyMap defaultProperties = new DefaultPropertyMap();
            MethodMap<Object> methods = new MethodMap<Object>();
            ASTNode[] propertyNodes = children[3].getChildren();
            ASTNode[] functionNodes = children[4].getChildren();

            for (int i = 0; i < propertyNodes.length; i++) {
                String typeName
                    = propertyNodes[i].getChild(0).getChild(0).getValue();
                String propertyName
                    = propertyNodes[i].getChild(1).getChild(0).getValue();
                CreatedObject evaluatedValue
                    = evaluateASTNode(propertyNodes[i].getChild(2), context);

                if (
                    RuntimeContext.getClass(typeName)
                        != evaluatedValue.getObjectClass()
                ) {
                    throw new RuntimeNodeException(
                        "Type `" + typeName + "` does not match found type."
                    );
                }

                defaultProperties.put(propertyName, evaluatedValue);
            }

            for (int i = 0; i < functionNodes.length; i++) {
                String functionName
                    = functionNodes[i].getChild(0).getChild(0).getValue();
                ASTNode[] parameterNodes
                    = functionNodes[i].getChild(1).getChildren();
                String returnTypeName
                    = functionNodes[i].getChild(2).getChild(0).getValue();
                ASTNode body = functionNodes[i].getChild(3);

                ParameterMap parameters = new ParameterMap();

                for (int j = 0; j < parameterNodes.length; j++) {
                    String typeName
                        = parameterNodes[j].getChild(0).getChild(0).getValue();
                    String parameterName
                        = parameterNodes[j].getChild(1).getChild(0).getValue();

                    parameters.put(parameterName, RuntimeContext.getClass(typeName));
                }

                methods.put(
                    functionName,
                    new CreatedFunction<Object>(
                        parameters,
                        body,
                        RuntimeContext.getClass(returnTypeName),
                        functionName
                    )
                );
            }

            RuntimeContext.setClass(
                className,
                new CreatedClass<Object>(defaultProperties, methods, className)
            );
        }

        if (node instanceof ASTGenerated_function_declaration) {
            String functionName
                = node.getChild(0).getChild(0).getValue();
            ASTNode[] parameterNodes
                = node.getChild(1).getChildren();
            String returnTypeName
                = node.getChild(2).getChild(0).getValue();
            ASTNode body = node.getChild(3);

            ParameterMap parameters = new ParameterMap();

            for (int j = 0; j < parameterNodes.length; j++) {
                String typeName
                    = parameterNodes[j].getChild(0).getChild(0).getValue();
                String parameterName
                    = parameterNodes[j].getChild(1).getChild(0).getValue();

                parameters.put(parameterName, RuntimeContext.getClass(typeName));
            }

            RuntimeContext.setGlobalFunction(
                functionName,
                new CreatedFunction<Object>(
                    parameters,
                    body,
                    RuntimeContext.getClass(returnTypeName),
                    functionName
                )
            );
        }
    }

    public static CreatedObject runASTNodes(ASTNode[] nodes, RuntimeContext context, boolean isInFunction) {
        for (int i = 0; i < nodes.length; i++) {
            if (isInFunction) {
                if (nodes[i] instanceof ASTGenerated_return_statement) {
                    return evaluateASTNode(nodes[i].getChildren()[0], context);
                }
            }

            runASTNode(nodes[i], context, isInFunction);
        }

        if (isInFunction) {
            return RuntimeContext.getGlobal("und");
        } else {
            return null;
        }
    }

    public static CreatedObject evaluateASTNode(
        ASTNode node,
        RuntimeContext context
    ) {
        if (
            node instanceof ASTGenerated_value
                || node instanceof ASTGenerated_inner_value
        ) {
            return evaluateASTNode(node.getChildren()[0], context);
        }

        if (node instanceof ASTGenerated_literal) {
            return evaluateASTNode(node.getChildren()[0], context);
        }

        if (node instanceof ASTGenerated_literal_boolean) {
            if (node.getValue().equals("true")) {
                return RuntimeConstants.getBlnClass().createObject(true);
            }

            if (node.getValue().equals("false")) {
                return RuntimeConstants.getBlnClass().createObject(false);
            }

            throw new RuntimeNodeException(
                "Expected `true` or `false` but found `"  + node.getValue() + "`."
            );
        }

        if (node instanceof ASTGenerated_literal_string) {
            if (!node.getValue().startsWith("'")) {
                throw new RuntimeNodeException(
                    "Expected string literal beginning with `'`, but none was found."
                );
            }

            if (!node.getValue().endsWith("'")) {
                throw new RuntimeNodeException(
                    "Expected string literal ending with `'`, but none was found."
                );
            }

            return RuntimeConstants.getStrClass().createObject(
                node.getValue().substring(1, node.getValue().length() - 1)
            );
        }

        if (node instanceof ASTGenerated_literal_int) {
            return RuntimeConstants.getIntClass().createObject(
                Long.parseLong(node.getValue())
            );
        }

        if (node instanceof ASTGenerated_normal_name) {
            return context.getObject(node.getChild(0).getValue());
        }

        if (node instanceof ASTGenerated_literal_float) {
            return RuntimeConstants.getFltClass().createObject(
                Double.parseDouble(node.getValue())
            );
        }

        if (
            node instanceof ASTGenerated_expression_with_parenthesis
                || node instanceof ASTGenerated_expression_without_parenthesis
        ) {
            ASTNode[] expressionChildren = node.getChildren();
            List<Object> expressionChildrenRemaining
                = new ArrayList<Object>(Arrays.asList(expressionChildren));
            int expressionSteps = (expressionChildren.length - 1) / 2;
            CreatedObject currentValue = null;

            while (1 < expressionChildrenRemaining.size()) {
                int currentHighestPrecedence = -1;
                int currentHighestPrecedenceIndex = -1;

                // When looping through, it can be assumed that all odd elements
                // are binary operators, while even ones are nodes or objects.
                // When elements are removed and added, this system is preserved.
                for (int i = 1; i < expressionChildrenRemaining.size(); i += 2) {
                    int precedence = precedenceForBinaryOperator(
                        (ASTNode) expressionChildrenRemaining.get(i)
                    );

                    if (currentHighestPrecedence < precedence) {
                        currentHighestPrecedence = precedence;
                        currentHighestPrecedenceIndex = i;
                    }
                }

                CreatedObject newObject = evaluateObjectExpressionStep(
                    expressionChildrenRemaining.get(currentHighestPrecedenceIndex - 1),
                    (ASTNode) expressionChildrenRemaining.get(currentHighestPrecedenceIndex),
                    expressionChildrenRemaining.get(currentHighestPrecedenceIndex + 1),
                    context
                );

                expressionChildrenRemaining.remove(currentHighestPrecedenceIndex - 1);
                expressionChildrenRemaining.remove(currentHighestPrecedenceIndex - 1);
                expressionChildrenRemaining.remove(currentHighestPrecedenceIndex - 1);
                expressionChildrenRemaining.add(currentHighestPrecedenceIndex - 1, newObject);
            }

            return (CreatedObject) expressionChildrenRemaining.get(0);
        }

        return RuntimeContext.getGlobal("und");
    }

    private static CreatedObject evaluateObjectExpressionStep(
        Object term0,
        ASTNode operatorNode,
        Object term1,
        RuntimeContext context
    ) {
        CreatedObject term0Created;
        CreatedObject term1Created;

        if (term0 instanceof ASTNode) {
            term0Created = evaluateASTNode((ASTNode) term0, context);
        } else if (term0 instanceof CreatedObject) {
            term0Created = (CreatedObject) term0;
        } else {
            throw new RuntimeNodeException(
                "Term 0 was neither a node nor a created object."
            );
        }

        if (term1 instanceof ASTNode) {
            term1Created = evaluateASTNode((ASTNode) term1, context);
        } else if (term1 instanceof CreatedObject) {
            term1Created = (CreatedObject) term1;
        } else {
            throw new RuntimeNodeException(
                "Term 1 was neither a node nor a created object."
            );
        }

        return evaluateExpressionStep(
            term0Created,
            operatorNode,
            term1Created,
            context
        );
    }

    private static CreatedObject evaluateExpressionStep(
        CreatedObject term0,
        ASTNode operatorNode,
        CreatedObject term1,
        RuntimeContext context
    ) {
        return term0.callMethod(
            methodNameForBinaryOperator(operatorNode),
            new CreatedObject[] { term1 }
        );
    }

    private static String methodNameForBinaryOperator(ASTNode operatorNode) {
        if (operatorNode instanceof ASTGenerated_binary_operator) {
            operatorNode = operatorNode.getChild(0);
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_equality) {
            return "equals";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_plus) {
            return "add";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_minus) {
            return "subtract";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_times) {
            return "multiply";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_divide) {
            return "divide";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_modulo) {
            return "modulo";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_exponential) {
            return "exponent";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_and) {
            return "and";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_or) {
            return "or";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_greater_than) {
            return "isGreater";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_less_than) {
            return "isLess";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_greater_or_equal) {
            return "isGreaterOrEqual";
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_less_or_equal) {
            return "isLessOrEqual";
        }

        throw new RuntimeNodeException("Binary operator found is unsupported.");
    }

    private static int precedenceForBinaryOperator(ASTNode operatorNode) {
        if (operatorNode instanceof ASTGenerated_binary_operator) {
            operatorNode = operatorNode.getChild(0);
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_equality) {
            return 30;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_plus) {
            return 40;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_minus) {
            return 40;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_times) {
            return 50;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_divide) {
            return 50;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_modulo) {
            return 50;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_exponential) {
            return 60;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_and) {
            return 20;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_or) {
            return 10;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_greater_than) {
            return 30;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_less_than) {
            return 30;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_greater_or_equal) {
            return 30;
        }

        if (operatorNode instanceof ASTGenerated_binary_operator_less_or_equal) {
            return 30;
        }

        throw new RuntimeNodeException("Binary operator found is unsupported.");
    }
}

class RuntimeNodeException extends RuntimeException {
    public RuntimeNodeException(String message) {
        super(message);
    }
}
