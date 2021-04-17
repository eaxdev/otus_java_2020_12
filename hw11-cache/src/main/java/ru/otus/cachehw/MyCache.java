package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> cache = new WeakHashMap<>();

    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key,value, "put");
    }

    @Override
    public void remove(K key) {
        var removedValue = cache.remove(key);
        notify(key, removedValue, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(it -> listener == it.get());
    }

    private void notify(K key, V value, String action) {
        var iterator = listeners.iterator();

        while (iterator.hasNext()) {
            try {
                var reference = iterator.next();
                var ref = reference.get();
                if (ref != null) {
                    ref.notify(key, value, action);
                } else {
                    iterator.remove();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
