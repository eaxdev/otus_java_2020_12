package ru.otus;

import ru.otus.core.meta.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl implements EntityClassMetaData {

    private final Class<?> clazz;

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Constructor<T> getConstructor() {
        try {
            return (Constructor<T>)clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> !it.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
