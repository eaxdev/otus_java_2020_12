package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.hibernate.model.AddressDataSet;
import ru.otus.hibernate.model.Client;
import ru.otus.hibernate.model.PhoneDataSet;
import ru.otus.hibernate.repository.DataTemplateHibernate;
import ru.otus.hibernate.repository.HibernateUtils;
import ru.otus.hibernate.service.DbServiceClientCachingProxy;
import ru.otus.hibernate.service.DbServiceClientImpl;
import ru.otus.hibernate.sessionmanager.TransactionManagerHibernate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.otus.hibernate.DbServiceDemo.HIBERNATE_CFG_FILE;

public class HibernateWithCacheMain {

    private static final Logger log = LoggerFactory.getLogger(HibernateWithCacheMain.class);

    public static void main(String[] args) {
        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, clientId:{}, action: {}", key, value == null ? null : value.getId(), action);
            }
        };

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var client = createClient(dbServiceClient);

        log.info("----------Without cache----------");
        long startTimeWithoutCache = System.nanoTime();
        var client1 = dbServiceClient.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        long estimatedTimeWithoutCache = System.nanoTime() - startTimeWithoutCache;
        log.info("Without cache. Client: {}, Time: {}", client1, estimatedTimeWithoutCache);

        log.info("----------With cache----------");
        var dbServiceClientCachingProxy = new DbServiceClientCachingProxy(dbServiceClient, new MyCache<>(), listener);
        //первый раз берем из БД
        var clientFromDb = dbServiceClientCachingProxy.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));

        long startTimeWithCache = System.nanoTime();
        var clientFromCache = dbServiceClientCachingProxy.getClient(clientFromDb.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientFromDb.getId()));
        long estimatedTimeWithCache = System.nanoTime() - startTimeWithCache;
        log.info("With cache. Client: {}, Time: {}", clientFromCache, estimatedTimeWithCache);

        log.info("Time without cache: {}, time with cache: {}. Different: {}",
                TimeUnit.MILLISECONDS.convert(estimatedTimeWithoutCache, TimeUnit.NANOSECONDS),
                TimeUnit.MILLISECONDS.convert(estimatedTimeWithCache, TimeUnit.NANOSECONDS),
                estimatedTimeWithoutCache / estimatedTimeWithCache);

    }

    private static Client createClient(DbServiceClientImpl dbServiceClient) {
        var client1 = new Client("dbServiceFirst");

        var addressDataSet1 = new AddressDataSet();
        addressDataSet1.setStreet("street1");
        client1.setAddressDataSet(addressDataSet1);
        addressDataSet1.setClient(client1);

        var phoneDataSet1 = new PhoneDataSet("+7999");
        phoneDataSet1.setClient(client1);
        var phoneDataSet2 = new PhoneDataSet("+7998");
        phoneDataSet2.setClient(client1);

        client1.setPhoneDataSet(List.of(phoneDataSet1, phoneDataSet2));

        return dbServiceClient.saveClient(client1);
    }

}
