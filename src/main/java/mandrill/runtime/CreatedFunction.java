package mandrill.runtime;

import java.util.LinkedHashMap;
import java.util.function.Function;

import mandrill.parser.ASTNode;

public class CreatedFunction<B> {
    private ParameterMap parameters;
    private ASTNode body;
    private RuntimeFunction baseBody;
    private CreatedClass returnClass;

    public CreatedFunction(
        ParameterMap parameters,
        ASTNode body,
        CreatedClass returnClass
    ) {
        this.parameters = parameters;
        this.body = body;
        this.returnClass = returnClass;
    }

    public CreatedFunction(
        ParameterMap parameters,
        RuntimeFunction baseBody,
        CreatedClass returnClass
    ) {
        this.parameters = parameters;
        this.baseBody = baseBody;
        this.returnClass = returnClass;
    }

    public CreatedObject call(
        CreatedObject<B> receiver, CreatedObject[] arguments
    ) {
        RuntimeContext currentContext = new RuntimeContext(receiver);
        int i = 0;

        for (String parameterName : parameters.keySet()) {
            if (parameters.get(parameterName) != arguments[i].getObjectClass()) {
                throw new ArgumentException(
                    "Parameter " + i + " is not of the correct type."
                );
            }

            i++;
            currentContext.setLocal(parameterName, arguments[i]);
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