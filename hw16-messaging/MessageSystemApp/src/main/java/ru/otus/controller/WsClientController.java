package ru.otus.controller;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.dto.ClientDto;
import ru.otus.dto.CreateClientResponse;
import ru.otus.messaging.ClientRequestAndResponse;
import ru.otus.messaging.FrontendService;

import java.util.Collections;

@Controller
public class WsClientController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FrontendService frontendService;

    public WsClientController(
            final SimpMessagingTemplate simpMessagingTemplate,
            final FrontendService frontendService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.frontendService = frontendService;
    }

    @MessageMapping("/create")
    public void createClient(final ClientDto clientDto, @Header("simpSessionId") String sessionId) {
        frontendService.serveRequest(
                ClientRequestAndResponse.newCreationRequest(clientDto),
                response -> simpMessagingTemplate.convertAndSendToUser(
                        sessionId,
                        "/queue/reply/create",
                        response.isCompletedSuccessfully() ? new CreateClientResponse(CreateClientResponse.Status.OK) :
                                new CreateClientResponse(CreateClientResponse.Status.FAIL),
                        createHeaders(sessionId)
                )
        );
    }

    @MessageMapping("/clients")
    public void clients(@Header("simpSessionId") String sessionId) {
        frontendService.serveRequest(
                ClientRequestAndResponse.newGetALlClientsRequest(),
                response -> simpMessagingTemplate.convertAndSendToUser(
                        sessionId,
                        "/queue/reply/clients",
                        response.isCompletedSuccessfully() ? response.getClients() : Collections.emptyList(),
                        createHeaders(sessionId)
                )
        );
    }

    private MessageHeaders createHeaders(String sessionId) {
        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        return headerAccessor.getMessageHeaders();
    }

}
