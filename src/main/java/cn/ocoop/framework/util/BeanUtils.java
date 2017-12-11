package cn.ocoop.framework.util;

import java.lang.reflect.Method;

/**
 * Created by liolay on 2017/12/5.
 */
public class BeanUtils {
    public static Method findMethod(Class<?> clazz, String methodName, Class... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException var4) {
            return findDeclaredMethod(clazz, methodName, paramTypes);
        }
    }

    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException var4) {
            return clazz.getSuperclass() != null?findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes):null;
        }
    }

    public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName) throws IllegalArgumentException {
        Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
        if(targetMethod == null) {
            targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
        }

        return targetMethod;
    }

    public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName) throws IllegalArgumentException {
        Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
        if(targetMethod == null && clazz.getSuperclass() != null) {
            targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
        }

        return targetMethod;
    }

    public static Method findMethodWithMinimalParameters(Method[] methods, String methodName) throws IllegalArgumentException {
        Method targetMethod = null;
        int numMethodsFoundWithCurrentMinimumArgs = 0;
        Method[] var4 = methods;
        int var5 = methods.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            if(method.getName().equals(methodName)) {
                int numParams = method.getParameterTypes().length;
                if(targetMethod != null && numParams >= targetMethod.getParameterTypes().length) {
                    if(!method.isBridge() && targetMethod.getParameterTypes().length == numParams) {
                        if(targetMethod.isBridge()) {
                            targetMethod = method;
                        } else {
                            ++numMethodsFoundWithCurrentMinimumArgs;
                        }
                    }
                } else {
                    targetMethod = method;
                    numMethodsFoundWithCurrentMinimumArgs = 1;
                }
            }
        }

        if(numMethodsFoundWithCurrentMinimumArgs > 1) {
            throw new IllegalArgumentException("Cannot resolve method '" + methodName + "' to a unique method. Attempted to resolve to overloaded method with the least number of parameters but there were " + numMethodsFoundWithCurrentMinimumArgs + " candidates.");
        } else {
            return targetMethod;
        }
    }
}
