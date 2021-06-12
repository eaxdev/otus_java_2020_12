package ru.otus.messaging;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.MessageType;

public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void serveRequest(
            ClientRequestAndResponse request,
            MessageCallback<ClientRequestAndResponse> onComplete) {
        var message = msClient.produceMessage(
                databaseServiceClientName,
                request,
                MessageType.USER_DATA,
                onComplete
        );
        msClient.sendMessage(message);
    }
}
