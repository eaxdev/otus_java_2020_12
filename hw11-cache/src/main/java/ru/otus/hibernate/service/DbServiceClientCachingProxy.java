package ru.otus.hibernate.service;

import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.hibernate.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCachingProxy implements DBServiceClient{

    private final HwCache<String, Client> cache = new MyCache<>();

    private final DBServiceClient dbServiceClient;

    public DbServiceClientCachingProxy(DbServiceClientImpl dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @SafeVarargs
    public DbServiceClientCachingProxy(DbServiceClientImpl dbServiceClient, HwListener<String, Client>... cacheListeners) {
        this.dbServiceClient = dbServiceClient;

        for (var cacheListener : cacheListeners) {
            this.cache.addListener(cacheListener);
        }
    }

    @Override
    public Client saveClient(Client client) {
        return dbServiceClient.saveClient(client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        var key = String.valueOf(id);
        var clientInCache = cache.get(key);
        if (clientInCache == null) {
            var client = dbServiceClient.getClient(id);
            client.ifPresent(value -> cache.put(key, value));
            return client;
        }

        return Optional.of(clientInCache);
    }

    @Override
    public List<Client> findAll() {
        //todo непонятно, как здесь использовать кэш. В данном случае нет ключа
        return dbServiceClient.findAll();
    }
}
