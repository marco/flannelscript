package mandrill.runtime;

import java.util.LinkedHashMap;

public class CreatedClass<B> {
    private DefaultPropertyMap defaultProperties;
    private MethodMap<B> methods;

    public CreatedClass(
        DefaultPropertyMap defaultProperties,
        MethodMap<B> methods
    ) {
        this.defaultProperties = defaultProperties;
        this.methods = methods;
    }

    public CreatedClass() {
        this(new DefaultPropertyMap(), new MethodMap<B>());
    }

    public CreatedObject<B> createObject() {
        return createObject(null);
    }

    public CreatedObject<B> createObject(B baseValue) {
        return new CreatedObject<B>(this, baseValue);
    }

    public CreatedFunction<B> getMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        throw new MethodNameException(
            "A method with name `" + name + "` could not be found."
        );
    }

    public boolean hasMethod(String name) {
        return methods.containsKey(name);
    }

    protected void addMethod(String name, CreatedFunction function) {
        if (methods.containsKey(name)) {
            throw new MethodNameException(
                "A method with name `" + name + "` already exists."
            );
        }

        methods.put(name, function);
    }

    public PropertyMap getPropertiesCopy() {
        PropertyMap properties = new PropertyMap();

        for (String defaultKey : defaultProperties.keySet()) {
            properties.put(
                defaultKey,
                new CreatedObject<Object>(properties.get(defaultKey))
            );
        }

        return properties;
    }
}

class DefaultPropertyMap extends LinkedHashMap<String, CreatedObject> {
}

class MethodMap<B> extends LinkedHashMap<String, CreatedFunction<B>> {
}

class MethodNameException extends RuntimeException {
    MethodNameException(String message) {
        super(message);
    }
}