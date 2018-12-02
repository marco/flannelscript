package flannelscript.runtime;

import java.util.LinkedHashMap;

public class RuntimeContext {
    private static LinkedHashMap<String, CreatedObject> globals
        = new LinkedHashMap<String, CreatedObject>();
    private static LinkedHashMap<String, CreatedFunction> globalFunctions
    = new LinkedHashMap<String, CreatedFunction>();
    private static LinkedHashMap<String, CreatedClass> classes
        = new LinkedHashMap<String, CreatedClass>();

    private CreatedObject openObject;
    private LinkedHashMap<String, CreatedObject> locals
        = new LinkedHashMap<String, CreatedObject>();

    public static void setGlobal(String name, CreatedObject createdObject) {
        globals.put(name, createdObject);
    }

    public static CreatedObject getGlobal(String name) {
        if (globals.containsKey(name)) {
            return globals.get(name);
        }

        return getGlobal("und");
    }

    public RuntimeContext(CreatedObject openObject) {
        this.openObject = openObject;
    }

    public void setLocal(String name, CreatedObject createdObject) {
        locals.put(name, createdObject);
    }

    public void updateObject(String name, CreatedObject createdObject) {
        if (locals.containsKey(name)) {
            locals.put(name, createdObject);
            return;
        }

        if (openObject.checkHasProperty(name)) {
            openObject.setProperty(name, createdObject);
            return;
        }

        if (globals.containsKey(name)) {
            globals.put(name, createdObject);
        }
    }

    public static void setClass(String name, CreatedClass createdClass) {
        classes.put(name, createdClass);
    }

    public CreatedObject getObject(String name) {
        if (locals.containsKey(name)) {
            return locals.get(name);
        }

        if (openObject.checkHasProperty(name)) {
            return openObject.getProperty(name);
        }

        if (globals.containsKey(name)) {
            return globals.get(name);
        }

        return getGlobal("und");
    }

    public CreatedFunction getFunction(String name) {
        if (
            openObject != null
                && openObject.getObjectClass().hasMethod(name)
        ) {
            return openObject.getObjectClass().getMethod(name);
        }

        if (globalFunctions.containsKey(name)) {
            return globalFunctions.get(name);
        }

        throw new RuntimeContextException(
            "A function with the name `" + name + "` could not be found."
        );
    }

    public CreatedObject getOpenObject() {
        return openObject;
    }

    public void setOpenObject(CreatedObject object) {
        openObject = object;
    }

    public static CreatedClass getClass(String name) {
        if (classes.containsKey(name)) {
            return classes.get(name);
        }

        throw new RuntimeContextException(
            "No class named `" + name + "` could be found."
        );
    }

    public static void setGlobalFunction(
        String name,
        CreatedFunction createdFunction
    ) {
        globalFunctions.put(name, createdFunction);
    }
    public Object getOpenBaseValue() {
        return openObject.getBaseValue();
    }
}

class RuntimeContextException extends RuntimeException {
    RuntimeContextException(String message) {
        super(message);
    }
}
