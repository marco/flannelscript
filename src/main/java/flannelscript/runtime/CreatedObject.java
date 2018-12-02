package flannelscript.runtime;

import java.util.LinkedHashMap;

public class CreatedObject<B> {
    private PropertyMap properties;
    private CreatedClass<B> objectClass;
    private B baseValue;

    CreatedObject(
        CreatedClass<B> objectClass,
        B baseValue
    ) {
        this.objectClass = objectClass;
        this.baseValue = baseValue;
        this.properties = objectClass.getPropertiesCopy();
    }

    CreatedObject(CreatedObject<B> original) {
        this.objectClass = original.objectClass;
        this.baseValue = original.getBaseValue();

        if (original.properties.size() == 0) {
            this.properties = new PropertyMap();
            return;
        }

        this.properties = original.getObjectClass().getPropertiesCopy();
    }

    public CreatedClass getObjectClass() {
        return objectClass;
    }

    public boolean checkHasProperty(String name) {
        return properties.containsKey(name);
    }

    public CreatedObject getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, CreatedObject propertyValue) {
        properties.put(name, propertyValue);
    }

    public CreatedObject callMethod(String name, CreatedObject[] arguments) {
        return objectClass.getMethod(name).call(this, arguments);
    }

    public B getBaseValue() {
        return baseValue;
    }
}

class PropertyMap extends LinkedHashMap<String, CreatedObject<Object>> {
}

class ObjectException extends RuntimeException {
    public ObjectException(String message) {
        super(message);
    }
}