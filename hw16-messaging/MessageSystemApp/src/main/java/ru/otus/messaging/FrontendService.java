package ru.otus.messaging;

import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {

    void serveRequest(ClientRequestAndResponse request, MessageCallback<ClientRequestAndResponse> onComplete);
}
