package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.AddressDataSet;
import ru.otus.model.PhoneDataSet;
import ru.otus.repository.DataTemplateHibernate;
import ru.otus.repository.HibernateUtils;
import ru.otus.sessionmanager.TransactionManagerHibernate;
import ru.otus.model.Client;
import ru.otus.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

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

        var savedClient = dbServiceClient.saveClient(client1);

        var clientSelected = dbServiceClient.getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client1.getId()));
        log.info("client1Selected:{}", clientSelected);

        clientSelected.setName("UpdatedName");
        clientSelected.getPhoneDataSet().removeIf(it -> it.getNumber().equals("+7998"));

        dbServiceClient.saveClient(clientSelected);
        var clientUpdated = dbServiceClient.getClient(clientSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(it -> log.info("client:{}", it));
    }
}
