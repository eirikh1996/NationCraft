package io.github.eirikh1996.nationcraft.api.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Class<?> getClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }
}
