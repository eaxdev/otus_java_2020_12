package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packageToScan) {
        var scanner = new Reflections(packageToScan, new SubTypesScanner(false));
        var initialConfigClasses = scanner.getSubTypesOf(Object.class)
                .stream().filter(it -> !it.isInterface() || !it.isAnnotation())
                .toArray(Class<?>[]::new);
        process(initialConfigClasses);
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        process(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        process(initialConfigClasses);
    }

    private void process(Class<?>... initialConfigClasses) {
        try {
            processConfig(initialConfigClasses);
        } catch (Exception e) {
            throw new RuntimeException("Can't create container", e);
        }
    }

    private void processConfig(Class<?>... configClasses) throws Exception {
        checkConfigClass(configClasses);
        for (var configClass : configClasses) {
            var sortedMethods = Arrays.stream(configClass.getMethods())
                    .filter(it -> it.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(it -> it.getAnnotation(AppComponent.class).order()))
                    .collect(Collectors.toList());

            var instance = configClass.getDeclaredConstructor().newInstance();

            for (var method : sortedMethods) {
                var parameters = method.getParameterTypes();
                var args = Arrays.stream(parameters).map(this::findComponent).toArray();

                var componentImpl = method.invoke(instance, args);
                var component = method.getReturnType().cast(componentImpl);

                var componentName = method.getAnnotation(AppComponent.class).name();
                appComponentsByName.put(componentName, component);
                appComponents.add(component);
            }
        }
    }

    private void checkConfigClass(Class<?>[] configClass) {
        for (Class<?> clazz : configClass) {
            if (!clazz.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", clazz.getName()));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        Object component = findComponent(componentClass);
        return (C) component;
    }

    private Object findComponent(Class<?> componentClass) {
        return appComponents.stream()
                .filter(componentClass::isInstance)
                .findFirst().orElseThrow(() -> new RuntimeException("Not found component"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException("Not found component"));
    }
}
