package mandrill.runtime;

import java.util.LinkedList;
import java.util.List;

import mandrill.parser.ASTNode;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_and;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_divide;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_equality;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_exponential;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_greater_or_equal;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_greater_than;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_less_or_equal;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_less_than;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_minus;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_modulo;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_or;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_plus;
import mandrill.parser.generatednodes.ASTGenerated_binary_operator_times;
import mandrill.parser.generatednodes.ASTGenerated_class_declaration;
import mandrill.parser.generatednodes.ASTGenerated_echo_statement;
import mandrill.parser.generatednodes.ASTGenerated_expression;
import mandrill.parser.generatednodes.ASTGenerated_function_call;
import mandrill.parser.generatednodes.ASTGenerated_function_declaration;
import mandrill.parser.generatednodes.ASTGenerated_if_statement;
import mandrill.parser.generatednodes.ASTGenerated_inside_function_action;
import mandrill.parser.generatednodes.ASTGenerated_literal;
import mandrill.parser.generatednodes.ASTGenerated_literal_boolean;
import mandrill.parser.generatednodes.ASTGenerated_literal_float;
import mandrill.parser.generatednodes.ASTGenerated_literal_int;
import mandrill.parser.generatednodes.ASTGenerated_literal_string;
import mandrill.parser.generatednodes.ASTGenerated_normal_name;
import mandrill.parser.generatednodes.ASTGenerated_return_statement;
import mandrill.parser.generatednodes.ASTGenerated_statement_call;
import mandrill.parser.generatednodes.ASTGenerated_value;
import mandrill.parser.generatednodes.ASTGenerated_variable_assignment;
import mandrill.parser.generatednodes.ASTGenerated_variable_declaration;
import mandrill.parser.generatednodes.ASTGenerated_while_statement;

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
                        RuntimeContext.getClass(returnTypeName)
                    )
                );
            }

            RuntimeContext.setClass(
                className,
                new CreatedClass<Object>(defaultProperties, methods)
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
                    RuntimeContext.getClass(returnTypeName)
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
        if (node instanceof ASTGenerated_value) {
            return evaluateASTNode(node.getChildren()[0], context);
        }

        if (node instanceof ASTGenerated_literal) {
            return evaluateASTNode(node.getChildren()[0], context);
        }

        if (node instanceof ASTGenerated_literal_boolean) {
            if (node.getValue() == "true") {
                return RuntimeConstants.getBlnClass().createObject(true);
            }

            if (node.getValue() == "false") {
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

        if (node instanceof ASTGenerated_expression) {
            ASTNode[] expressionChildren = node.getChildren()[0].getChildren();

            ASTNode operatorNode = expressionChildren[1];

            if (operatorNode instanceof ASTGenerated_binary_operator_equality) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "equals",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_plus) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "add",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_minus) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "subtract",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_times) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "multiply",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_divide) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "divide",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_modulo) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "modulo",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_exponential) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "exponent",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_and) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "and",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_or) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "or",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_greater_than) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "isGreater",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_less_than) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "isLess",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_greater_or_equal) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "isGreaterOrEqual",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }

            if (operatorNode instanceof ASTGenerated_binary_operator_less_or_equal) {
                return evaluateASTNode(expressionChildren[0], context).callMethod(
                    "isLessOrEqual",
                    new CreatedObject[] { evaluateASTNode(expressionChildren[1], context) }
                );
            }
        }

        return RuntimeContext.getGlobal("und");
    }
}

class RuntimeNodeException extends RuntimeException {
    public RuntimeNodeException(String message) {
        super(message);
    }
}
