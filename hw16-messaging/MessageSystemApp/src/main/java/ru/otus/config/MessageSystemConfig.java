package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messaging.ClientRequestHandler;
import ru.otus.messaging.ClientResponseHandler;
import ru.otus.messaging.FrontendService;
import ru.otus.messaging.FrontendServiceImpl;
import ru.otus.service.ClientService;

@Configuration
public class MessageSystemConfig {

    private static final String DB_CLIENT_NAME = "database";
    private static final String FRONT_CLIENT_NAME = "frontend";

    @Bean
    public FrontendService frontendService(ClientService clientService) {
        var messageSystem = new MessageSystemImpl();
        var callbackRegistry = new CallbackRegistryImpl();
        var handlersStoreForDbClient = new HandlersStoreImpl();
        handlersStoreForDbClient.addHandler(MessageType.USER_DATA, new ClientRequestHandler(clientService));
        messageSystem.addClient(new MsClientImpl(DB_CLIENT_NAME, messageSystem, handlersStoreForDbClient, callbackRegistry)
        );
        var handlerForFrontendClient = new HandlersStoreImpl();
        handlerForFrontendClient.addHandler(MessageType.USER_DATA, new ClientResponseHandler(callbackRegistry));
        var frontClient = new MsClientImpl(
                FRONT_CLIENT_NAME,
                messageSystem,
                handlerForFrontendClient,
                callbackRegistry
        );
        messageSystem.addClient(frontClient);
        return new FrontendServiceImpl(frontClient, DB_CLIENT_NAME);
    }

}
