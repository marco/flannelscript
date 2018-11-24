package mandrill.runtime;

import java.util.LinkedHashMap;

public class CreatedClass<B> {
    private DefaultPropertyMap overridenProperties;
    private DefaultPropertyMap defaultProperties;
    private MethodMap<B> methods;
    private String name;
    private CreatedClass extendsClass;

    public CreatedClass(
        DefaultPropertyMap overridenProperties,
        DefaultPropertyMap defaultProperties,
        MethodMap<B> methods,
        String name,
        CreatedClass extendsClass
    ) {
        this.overridenProperties = overridenProperties;
        this.defaultProperties = defaultProperties;
        this.methods = methods;
        this.name = name;
        this.extendsClass = extendsClass;
    }

    public CreatedClass(
        DefaultPropertyMap defaultProperties,
        MethodMap<B> methods,
        String name,
        CreatedClass extendsClass
    ) {
        this(new DefaultPropertyMap(), defaultProperties, methods, name, extendsClass);
    }

    public CreatedClass(String name, CreatedClass extendsClass) {
        this(new DefaultPropertyMap(), new MethodMap<B>(), name, extendsClass);
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

        if (extendsClass != null) {
            return extendsClass.getMethod(name);
        }

        throw new MethodNameException(
            "A method with name `" + name + "` could not be found."
        );
    }

    public boolean hasMethod(String name) {
        if (extendsClass == null) {
            return methods.containsKey(name);
        }

        return methods.containsKey(name) || extendsClass.hasMethod(name);
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

        if (extendsClass != null) {
            properties.putAll(extendsClass.getPropertiesCopy());
        }

        for (String overrideKey : overridenProperties.keySet()) {
            if (!properties.containsKey(overrideKey)) {
                throw new OverrideException(
                    "Overridden key `" + overrideKey + "` does not exist."
                );
            }

            if (
                properties.get(overrideKey).getObjectClass()
                    != overridenProperties.get(overrideKey).getObjectClass()
            ) {
                throw new OverrideException(
                    "Expected type `"
                        + properties.get(overrideKey).getObjectClass().toString()
                        + "`, but found `"
                        + overridenProperties.get(overrideKey).getObjectClass()
                        + "`."
                );
            }

            properties.put(
                overrideKey,
                new CreatedObject<Object>(overridenProperties.get(overrideKey))
            );
        }

        for (String defaultKey : defaultProperties.keySet()) {
            properties.put(
                defaultKey,
                new CreatedObject<Object>(defaultProperties.get(defaultKey))
            );
        }

        return properties;
    }

    @Override
    public String toString() {
        return name;
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

class OverrideException extends RuntimeException {
    OverrideException(String message) {
        super(message);
    }
}
