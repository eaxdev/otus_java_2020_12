package ru;

import org.reflections.Reflections;

import java.util.Set;

public class Scanner {

    private final Reflections scanner;

    public Scanner(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
    }

    public <T> Class<? extends T> getImplClass(Class<T> iFace) {
        final Set<Class<? extends T>> classes = scanner.getSubTypesOf(iFace);
        if (classes.size() != 1) {
            throw new RuntimeException(iFace + " has 0 or more than one implementations");
        }

        return classes.iterator().next();
    }

}
