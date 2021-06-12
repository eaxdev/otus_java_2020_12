package ru.otus.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.service.ClientService;

import java.util.Optional;

public class ClientRequestHandler implements RequestHandler<ClientRequestAndResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRequestHandler.class);

    private final ClientService clientService;

    public ClientRequestHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ClientRequestAndResponse request = MessageHelper.getPayload(msg);
        try {
            if (request.getType() == RequestType.CREATE) {
                final var clientDto = request.getClient();
                clientService.save(clientDto);
                return Optional.of(MessageBuilder.buildReplyMessage(msg, request.withStatus(true)));
            } else {
                final var clients = clientService.findAll();
                return Optional.of(MessageBuilder.buildReplyMessage(msg, request.withFetchedClients(clients)));
            }
        } catch (final Exception ex) {
            LOGGER.error("Error while working with client service.", ex);
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, request.withStatus(false)));
    }
}