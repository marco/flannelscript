package mandrill.runtime;

import java.util.LinkedHashMap;
import java.util.function.Function;

import mandrill.parser.ASTNode;

public class CreatedFunction<B> {
    private ParameterMap parameters;
    private ASTNode body;
    private RuntimeFunction baseBody;
    private CreatedClass returnClass;
    private String name;

    public CreatedFunction(
        ParameterMap parameters,
        ASTNode body,
        CreatedClass returnClass,
        String name
    ) {
        this.parameters = parameters;
        this.body = body;
        this.returnClass = returnClass;
        this.name = name;
    }

    public CreatedFunction(
        ParameterMap parameters,
        RuntimeFunction baseBody,
        CreatedClass returnClass,
        String name
    ) {
        this.parameters = parameters;
        this.baseBody = baseBody;
        this.returnClass = returnClass;
        this.name = name;
    }

    public CreatedObject call(
        CreatedObject<B> receiver, CreatedObject[] arguments
    ) {
        RuntimeContext currentContext = new RuntimeContext(receiver);
        int i = 0;

        if (arguments.length != parameters.size()) {
            throw new ArgumentException(
                "Expected " + parameters.size() + "arguments, but "
                    + arguments.length + " were found."
            );
        }

        for (String parameterName : parameters.keySet()) {
            if (parameters.get(parameterName) != arguments[i].getObjectClass()) {
                throw new ArgumentException(
                    "Parameter " + i + " is not of the correct type. Expected type `"
                        + parameters.get(parameterName).toString() + "` but found `"
                        + arguments[i].getObjectClass().toString() + "` for function `"
                        + name + "`."
                );
            }

            currentContext.setLocal(parameterName, arguments[i]);
            i++;
        }

        if (baseBody != null) {
            return baseBody.apply(currentContext);
        }

        CreatedObject result = RuntimeNode.runASTNodes(
            body.getChildren(),
            currentContext,
            true
        );

        if (returnClass == RuntimeContext.getClass("Void")) {
            if (result != RuntimeContext.getGlobal("und")) {
                throw new ReturnException(
                    "Function returned a result, but `und` was expected."
                );
            }
        } else {
            if (
                result.getObjectClass() != returnClass
                    && result != RuntimeContext.getGlobal("nil")
                    || result == RuntimeContext.getGlobal("und")
            ) {
                throw new ReturnException(
                    "An incorrect return type was found."
                );
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}

class ArgumentException extends RuntimeException {
    ArgumentException(String message) {
        super(message);
    }
}

class ReturnException extends RuntimeException {
    ReturnException(String message) {
        super(message);
    }
}

class ParameterMap extends LinkedHashMap<String, CreatedClass> {
}

interface RuntimeFunction extends Function<RuntimeContext, CreatedObject> {
}
