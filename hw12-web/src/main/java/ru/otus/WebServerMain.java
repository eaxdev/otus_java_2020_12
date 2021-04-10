package ru.otus;

import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.hibernate.model.AddressDataSet;
import ru.otus.hibernate.model.Client;
import ru.otus.hibernate.model.PhoneDataSet;
import ru.otus.hibernate.repository.DataTemplateHibernate;
import ru.otus.hibernate.repository.HibernateUtils;
import ru.otus.hibernate.service.DBServiceClient;
import ru.otus.hibernate.service.DbServiceClientImpl;
import ru.otus.hibernate.sessionmanager.TransactionManagerHibernate;
import ru.otus.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.ClientAuthServiceImpl;

import java.util.List;

public class WebServerMain {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        var dbServiceClient = createDbServiceClient();
        createClient(dbServiceClient);
        var gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();

        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var authService = new ClientAuthServiceImpl(dbServiceClient);

        var clientWebServer = new ClientsWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceClient, gson, templateProcessor);

        clientWebServer.start();
        clientWebServer.join();
    }

    private static void createClient(DBServiceClient dbServiceClient) {
        var client1 = new Client("Иванов Иван", "client1", "111");

        var addressDataSet1 = new AddressDataSet();
        addressDataSet1.setStreet("street1");
        client1.setAddressDataSet(addressDataSet1);
        addressDataSet1.setClient(client1);

        var phoneDataSet1 = new PhoneDataSet("+7999");
        phoneDataSet1.setClient(client1);
        var phoneDataSet2 = new PhoneDataSet("+7998");
        phoneDataSet2.setClient(client1);

        client1.setPhoneDataSet(List.of(phoneDataSet1, phoneDataSet2));

        dbServiceClient.saveClient(client1);
    }

    private static DBServiceClient createDbServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

}
