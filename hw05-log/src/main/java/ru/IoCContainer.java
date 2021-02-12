package ru;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IoCContainer {

    private final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

    private final ProxyCreator proxyCreator;

    private final Scanner scanner;

    public IoCContainer(Scanner scanner) {
        this.scanner = scanner;
        proxyCreator = new ProxyCreator();
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(Class<T> type) {
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }

        Class<? extends T> implClass = type;

        if (type.isInterface()) {
            implClass = scanner.getImplClass(implClass);
        }

        Object obj = proxyCreator.createProxyIfNeed(implClass);
        cache.put(type, obj);

        return (T) obj;
    }

}
