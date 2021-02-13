package ru;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import ru.metadata.Log;
import ru.metadata.ProxyStrategy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProxyCreator {

    public Object createProxyIfNeed(Class<?> clazz) throws Exception {
        if (clazz.isAnnotationPresent(ProxyStrategy.class)) {
            ProxyStrategy.Strategy strategy = clazz.getAnnotation(ProxyStrategy.class).value();

            return switch (strategy) {
                case CGLIB -> cglib(clazz);
                case DYNAMIC_PROXY -> dynamicProxy(clazz);
            };
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    private Object cglib(Class<?> clazz) throws Exception {
        if (Arrays.stream(clazz.getMethods()).anyMatch(it -> it.isAnnotationPresent(Log.class))) {
            return Enhancer.create(clazz,
                    (InvocationHandler) (proxy, method, args) -> getInvocationHandler(clazz, method, args));
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    private Object dynamicProxy(Class<?> clazz) throws Exception {
        Map<String, List<Class<?>>> methodSignatures = Arrays.stream(clazz.getMethods())
                .filter(it -> it.isAnnotationPresent(Log.class))
                .collect(Collectors.toMap(k -> k.getName() + "_" + k.getParameters().length, v -> List.of(v.getParameterTypes())));
        if (methodSignatures.isEmpty()) {
            return clazz.getDeclaredConstructor().newInstance();
        } else {
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                    (proxy, method, args) -> {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        //тк в интерфейсе нет анноатции @Log ищем по методам из имплементации, которые заранее отложили
                        //Сравниваем по имени и аргументам метода
                        List<Class<?>> methodArgs = methodSignatures.get(method.getName() + "_" + method.getParameters().length);
                        if (methodArgs == null) {
                            return method.invoke(instance, args);
                        }
                        Class<?>[] methodArgsArray = methodArgs.toArray(new Class[0]);

                        //если есть полное совпадение по имени и типам аргументов
                        if (Arrays.equals(methodArgsArray, method.getParameterTypes())) {
                            System.out.printf("""
                                    executed method: %s, params: %s
                                    """, method.getName(), Arrays.toString(args));
                            return method.invoke(instance, args);
                        }

                        return method.invoke(instance, args);
                    });
        }
    }

    private Object getInvocationHandler(Class<?> clazz, Method method, Object[] args) throws Exception {
        Object instance = clazz.getDeclaredConstructor().newInstance();
        if (method.isAnnotationPresent(Log.class)) {
            System.out.printf("""
                    executed method: %s, params: %s
                    """, method.getName(), Arrays.toString(args));
            return method.invoke(instance, args);
        }

        return method.invoke(instance, args);
    }
}
