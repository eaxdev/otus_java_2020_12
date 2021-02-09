package ru;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import ru.metadata.Log;
import ru.metadata.ProxyStrategy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyCreator {

    public Object createProxyIfNeed(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ProxyStrategy.class)) {
            ProxyStrategy.Strategy strategy = clazz.getAnnotation(ProxyStrategy.class).value();

            return switch (strategy) {
                case CGLIB -> dynamicProxy(clazz);
                case DYNAMIC_PROXY -> cglib(clazz);
            };
        }
        return clazz;
    }

    private Object cglib(Class<?> clazz) {
        List<String> methodsWithLog = Arrays.stream(clazz.getMethods())
                .filter(it -> it.isAnnotationPresent(Log.class))
                .map(Method::getName)
                .collect(Collectors.toList());
        if (methodsWithLog.isEmpty()) {
            return clazz;
        } else {
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                    (proxy, method, args) -> getInvocationHandler(clazz, methodsWithLog, method, args));
        }
    }

    private Object dynamicProxy(Class<?> clazz) {
        if (Arrays.stream(clazz.getMethods()).anyMatch(it -> it.isAnnotationPresent(Log.class))) {
            List<String> methodsWithLog = Collections.emptyList();
            return Enhancer.create(clazz,
                    (InvocationHandler) (proxy, method, args) -> getInvocationHandler(clazz, methodsWithLog, method, args));
        }
        return clazz;
    }

    private Object getInvocationHandler(Class<?> clazz, List<String> methodsWithLog, Method method, Object[] args) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        Object instance = clazz.getDeclaredConstructor().newInstance();
        if (methodsWithLog.contains(method.getName()) || method.isAnnotationPresent(Log.class)) {
            System.out.printf("""
                    executed method: %s, params: %s
                    """, method.getName(), Arrays.toString(args));
            return method.invoke(instance, args);
        }

        return method.invoke(instance, args);
    }
}
