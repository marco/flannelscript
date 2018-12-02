package flannelscript.runtime;

public class RuntimeConstants {
    private static CreatedClass<Object> objClass;
    private static CreatedClass<Boolean> blnClass;
    private static CreatedClass<String> strClass;
    private static CreatedClass<Long> intClass;
    private static CreatedClass<Double> fltClass;

    public static void setGlobals() {
        objClass = new CreatedClass<Object>("Obj", null);
        CreatedClass<Object> voidClass = new CreatedClass<Object>("Void", null);
        RuntimeContext.setClass("Obj", objClass);
        RuntimeContext.setClass("Void", voidClass);
        RuntimeContext.setGlobal("und", objClass.createObject(null));
        RuntimeContext.setGlobal("nil", objClass.createObject(null));

        // Classes:

        // `Bln`:
        blnClass = new CreatedClass<Boolean>("Bln", objClass);

        // `Str`:
        strClass = new CreatedClass<String>("Str", objClass);

        // `Int`:
        intClass = new CreatedClass<Long>("Int", objClass);

        // `Flt`:
        fltClass = new CreatedClass<Double>("Flt", objClass);

        // Methods:

        // `equals`:

        RuntimeFunction equalsFunction = (RuntimeContext context) -> {
            if (context.getOpenBaseValue().equals(context.getObject("other").getBaseValue())) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(blnClass, "equals", equalsFunction);
        addBinaryFunctionToClass(strClass, "equals", equalsFunction);
        addBinaryFunctionToClass(intClass, "equals", equalsFunction);
        addBinaryFunctionToClass(fltClass, "equals", equalsFunction);

        // `doesNotEqual`:

        RuntimeFunction doesNotEqualFunction = (RuntimeContext context) -> {
            if (context.getOpenBaseValue().equals(context.getObject("other").getBaseValue())) {
                return blnClass.createObject(false);
            }

            return blnClass.createObject(true);
        };

        addBinaryFunctionToClass(blnClass, "doesNotEqual", doesNotEqualFunction);
        addBinaryFunctionToClass(strClass, "doesNotEqual", doesNotEqualFunction);
        addBinaryFunctionToClass(intClass, "doesNotEqual", doesNotEqualFunction);
        addBinaryFunctionToClass(fltClass, "doesNotEqual", doesNotEqualFunction);

        // `add` (`str`):

        RuntimeFunction strAddFunction = (RuntimeContext context) -> {
            String openString = (String) context.getOpenBaseValue();
            String otherString = (String) context.getObject("other").getBaseValue();

            return strClass.createObject(openString + otherString);
        };

        addBinaryFunctionToClass(strClass, "add", strAddFunction);

        // `add` (`int`):

        RuntimeFunction intAddFunction = (RuntimeContext context) -> {
            Long openLong = (Long) context.getOpenBaseValue();
            Long otherLong = (Long) context.getObject("other").getBaseValue();

            return intClass.createObject(openLong + otherLong);
        };

        addBinaryFunctionToClass(intClass, "add", intAddFunction);

        // `add` (`flt`):

        RuntimeFunction fltAddFunction = (RuntimeContext context) -> {
            Double openDouble = (Double) context.getOpenBaseValue();
            Double otherDouble = (Double) context.getObject("other").getBaseValue();

            return fltClass.createObject(openDouble + otherDouble);
        };

        addBinaryFunctionToClass(fltClass, "add", fltAddFunction);

        // `subtract` (`int`):

        RuntimeFunction intSubtractFunction = (RuntimeContext context) -> {
            Long openLong = (Long) context.getOpenBaseValue();
            Long otherLong = (Long) context.getObject("other").getBaseValue();

            return intClass.createObject(openLong - otherLong);
        };

        addBinaryFunctionToClass(intClass, "subtract", intSubtractFunction);

        // `subtract` (`flt`):

        RuntimeFunction fltSubtractFunction = (RuntimeContext context) -> {
            Double openDouble = (Double) context.getOpenBaseValue();
            Double otherDouble = (Double) context.getObject("other").getBaseValue();

            return fltClass.createObject(openDouble - otherDouble);
        };

        addBinaryFunctionToClass(fltClass, "subtract", fltSubtractFunction);

        // `multiply` (`int`):

        RuntimeFunction intTimesFunction = (RuntimeContext context) -> {
            Long openLong = (Long) context.getOpenBaseValue();
            Long otherLong = (Long) context.getObject("other").getBaseValue();

            return intClass.createObject(openLong * otherLong);
        };

        addBinaryFunctionToClass(intClass, "multiply", intTimesFunction);

        // `multiply` (`flt`):

        RuntimeFunction fltTimesFunction = (RuntimeContext context) -> {
            Double openDouble = (Double) context.getOpenBaseValue();
            Double otherDouble = (Double) context.getObject("other").getBaseValue();

            return fltClass.createObject(openDouble * otherDouble);
        };

        addBinaryFunctionToClass(fltClass, "multiply", fltTimesFunction);

        // `divide` (`int`):

        RuntimeFunction intDivideFunction = (RuntimeContext context) -> {
            Long openLong = (Long) context.getOpenBaseValue();
            Long otherLong = (Long) context.getObject("other").getBaseValue();

            return intClass.createObject(openLong / otherLong);
        };

        addBinaryFunctionToClass(intClass, "divide", intDivideFunction);

        // `divide` (`flt`):

        RuntimeFunction fltDivideFunction = (RuntimeContext context) -> {
            Double openDouble = (Double) context.getOpenBaseValue();
            Double otherDouble = (Double) context.getObject("other").getBaseValue();

            return fltClass.createObject(openDouble / otherDouble);
        };

        addBinaryFunctionToClass(fltClass, "divide", fltDivideFunction);

        // `modulo` (`int`):

        RuntimeFunction intModuloFunction = (RuntimeContext context) -> {
            Long openLong = (Long) context.getOpenBaseValue();
            Long otherLong = (Long) context.getObject("other").getBaseValue();

            return intClass.createObject(openLong % otherLong);
        };

        addBinaryFunctionToClass(intClass, "modulo", intModuloFunction);

        // `modulo` (`flt`):

        RuntimeFunction fltModuloFunction = (RuntimeContext context) -> {
            Double openDouble = (Double) context.getOpenBaseValue();
            Double otherDouble = (Double) context.getObject("other").getBaseValue();

            return fltClass.createObject(openDouble % otherDouble);
        };

        addBinaryFunctionToClass(fltClass, "modulo", fltModuloFunction);

        // `exponent` (`int`):

        RuntimeFunction intExponentFunction = (RuntimeContext context) -> {
            Long openLong = (Long) context.getOpenBaseValue();
            Long otherLong = (Long) context.getObject("other").getBaseValue();

            return intClass.createObject(openLong % otherLong);
        };

        addBinaryFunctionToClass(intClass, "exponent", intModuloFunction);

        // `and` (`bln`):

        RuntimeFunction andFunction = (RuntimeContext context) -> {
            if (
                (Boolean) context.getOpenBaseValue()
                    && (Boolean) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(blnClass, "and", andFunction);

        // `or` (`bln`):

        RuntimeFunction orFunction = (RuntimeContext context) -> {
            if (
                (Boolean) context.getOpenBaseValue()
                    || (Boolean) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(blnClass, "or", orFunction);

        // `isGreater` (`int`):

        RuntimeFunction intIsGreaterFunction = (RuntimeContext context) -> {
            if (
                (Long) context.getOpenBaseValue()
                    > (Long) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(intClass, "isGreater", intIsGreaterFunction);

        // `isGreater` (`flt`):

        RuntimeFunction fltIsGreaterFunction = (RuntimeContext context) -> {
            if (
                (Double) context.getOpenBaseValue()
                    > (Double) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(fltClass, "isGreater", fltIsGreaterFunction);

        // `isLess` (`int`):

        RuntimeFunction intIsLessFunction = (RuntimeContext context) -> {
            if (
                (Long) context.getOpenBaseValue()
                    < (Long) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(intClass, "isLess", intIsLessFunction);

        // `isLess` (`flt`):

        RuntimeFunction fltIsLessFunction = (RuntimeContext context) -> {
            if (
                (Double) context.getOpenBaseValue()
                    < (Double) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(fltClass, "isLess", fltIsLessFunction);

        // `isGreaterOrEqual` (`int`):

        RuntimeFunction intIsGreaterOrEqualFunction = (RuntimeContext context) -> {
            if (
                (Long) context.getOpenBaseValue()
                    >= (Long) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(intClass, "isGreaterOrEqual", intIsGreaterOrEqualFunction);

        // `isGreaterOrEqual` (`flt`):

        RuntimeFunction fltIsGreaterOrEqualFunction = (RuntimeContext context) -> {
            if (
                (Double) context.getOpenBaseValue()
                    >= (Double) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(fltClass, "isGreaterOrEqual", fltIsGreaterOrEqualFunction);

        // `isLessOrEqual` (`int`):

        RuntimeFunction intIsLessOrEqualFunction = (RuntimeContext context) -> {
            if (
                (Long) context.getOpenBaseValue()
                    <= (Long) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(intClass, "isLessOrEqual", intIsLessOrEqualFunction);

        // `isLessOrEqual` (`flt`):

        RuntimeFunction fltIsLessOrEqualFunction = (RuntimeContext context) -> {
            if (
                (Double) context.getOpenBaseValue()
                    <= (Double) context.getObject("other").getBaseValue()
            ) {
                return blnClass.createObject(true);
            }

            return blnClass.createObject(false);
        };

        addBinaryFunctionToClass(fltClass, "isLessOrEqual", fltIsLessOrEqualFunction);

        // `getStr` (`bln`):

        RuntimeFunction blnGetStr = (RuntimeContext context) -> {
            if ((Boolean) context.getOpenBaseValue() == true) {
                return strClass.createObject("true");
            }

            return strClass.createObject("false");
        };

        addSelfFunctionToClass(blnClass, "getStr", blnGetStr, strClass);

        // `getStr` (`int`):

        RuntimeFunction intGetStr = (RuntimeContext context) -> {
            return strClass.createObject(
                ((Long) context.getOpenBaseValue()).toString()
            );
        };

        addSelfFunctionToClass(intClass, "getStr", intGetStr, strClass);

        // `getStr` (`flt`):

        RuntimeFunction fltGetStr = (RuntimeContext context) -> {
            return strClass.createObject(
                ((Double) context.getOpenBaseValue()).toString()
            );
        };

        addSelfFunctionToClass(fltClass, "getStr", fltGetStr, strClass);

        // `getInt` (`Str`):

        RuntimeFunction strGetInt = (RuntimeContext context) -> {
            return intClass.createObject(
                Long.parseLong((String) context.getOpenBaseValue())
            );
        };

        addSelfFunctionToClass(strClass, "getInt", strGetInt, intClass);

        // `getInt` (`Str`):

        RuntimeFunction strGetFlt = (RuntimeContext context) -> {
            return fltClass.createObject(
                Double.parseDouble((String) context.getOpenBaseValue())
            );
        };

        addSelfFunctionToClass(strClass, "getFlt", strGetFlt, fltClass);

        // Adding classes:

        RuntimeContext.setClass("Bln", blnClass);
        RuntimeContext.setClass("Str", strClass);
        RuntimeContext.setClass("Int", intClass);
        RuntimeContext.setClass("Flt", fltClass);
    }

    static <B> void addBinaryFunctionToClass(CreatedClass<B> classToUse, String name, RuntimeFunction function) {
        ParameterMap parameters = new ParameterMap();
        parameters.put("other", classToUse);

        classToUse.addMethod(name, new CreatedFunction<B>(parameters, function, classToUse, name));
    }

    static <B, RB> void addSelfFunctionToClass(
        CreatedClass<B> classToUse,
        String name,
        RuntimeFunction function,
        CreatedClass<RB> returnType
    ) {
        ParameterMap parameters = new ParameterMap();
        classToUse.addMethod(name, new CreatedFunction<B>(parameters, function, returnType, name));
    }

    static CreatedClass<Object> getObjClass() {
        return objClass;
    }

    static CreatedClass<Boolean> getBlnClass() {
        return blnClass;
    }

    static CreatedClass<String> getStrClass() {
        return strClass;
    }

    static CreatedClass<Long> getIntClass() {
        return intClass;
    }

    static CreatedClass<Double> getFltClass() {
        return fltClass;
    }
}

class CreatedFunctionBln extends CreatedFunction<Boolean> {
    public CreatedFunctionBln(
        ParameterMap parameters,
        RuntimeFunction baseBody,
        CreatedClass returnType
    ) {
        super(parameters, baseBody, returnType);
    }
}

class CreatedFunctionStr extends CreatedFunction<String> {
    public CreatedFunctionStr(
        ParameterMap parameters,
        RuntimeFunction baseBody,
        CreatedClass returnType
    ) {
        super(parameters, baseBody, returnType);
    }
}

class CreatedFunctionInt extends CreatedFunction<Long> {
    public CreatedFunctionInt(
        ParameterMap parameters,
        RuntimeFunction baseBody,
        CreatedClass returnType
    ) {
        super(parameters, baseBody, returnType);
    }
}

class CreatedFunctionFlt extends CreatedFunction<Double> {
    public CreatedFunctionFlt(
        ParameterMap parameters,
        RuntimeFunction baseBody,
        CreatedClass returnType
    ) {
        super(parameters, baseBody, returnType);
    }
}

class BaseValueException extends RuntimeException {
    public BaseValueException(String message) {
        super(message);
    }
}
