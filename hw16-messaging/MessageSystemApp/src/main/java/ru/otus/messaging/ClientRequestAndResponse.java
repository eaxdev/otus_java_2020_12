package ru.otus.messaging;

import ru.otus.dto.ClientDto;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.model.AddressDataSet;
import ru.otus.model.Client;
import ru.otus.model.PhoneDataSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientRequestAndResponse extends ResultDataType {

    private final RequestType requestType;

    private final ClientDto clientDto;

    private final List<ClientDto> responseForGetAllRequest;

    private final boolean completedSuccessfully;

    private ClientRequestAndResponse(
            RequestType requestType,
            ClientDto clientDto,
            List<ClientDto> responseForGetAllRequest,
            boolean completedSuccessfully) {
        this.requestType = requestType;
        this.clientDto = clientDto;
        this.responseForGetAllRequest = responseForGetAllRequest;
        this.completedSuccessfully = completedSuccessfully;
    }

    public static ClientRequestAndResponse newCreationRequest(ClientDto clientDto) {
        return new ClientRequestAndResponse(RequestType.CREATE, clientDto, null, false);
    }

    public static ClientRequestAndResponse newGetALlClientsRequest() {
        return new ClientRequestAndResponse(RequestType.GET_ALL, null, null, false);
    }

    public ClientRequestAndResponse withFetchedClients(List<Client> clients) {
        var clientDtos = convertToDto(clients);
        return new ClientRequestAndResponse(requestType, null, clientDtos, true);
    }

    private List<ClientDto> convertToDto(List<Client> clients) {
        return clients.stream()
                .map(it -> {
                    var client = new ClientDto();
                    client.setAddress(it.getAddress().getStreet());
                    client.setName(it.getName());
                    client.setPhone(it.getPhoneDataSet().iterator().next().getNumber());
                    return client;
                })
                .collect(Collectors.toList());
    }

    public ClientRequestAndResponse withStatus(boolean isSuccess) {
        return new ClientRequestAndResponse(requestType, clientDto, null, isSuccess);
    }

    public Client getClient() {
        return convertToDomainModel(clientDto);
    }

    private Client convertToDomainModel(ClientDto clientDto) {
        return new Client(null, clientDto.getName(), null, null,
                new AddressDataSet(clientDto.getAddress()),
                Set.of(new PhoneDataSet(clientDto.getPhone())));
    }

    public RequestType getType() {
        return requestType;
    }

    public boolean isCompletedSuccessfully() {
        return completedSuccessfully;
    }

    public List<ClientDto> getClients() {
        return responseForGetAllRequest;
    }

}
