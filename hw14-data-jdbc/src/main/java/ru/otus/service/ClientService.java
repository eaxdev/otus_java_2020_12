package ru.otus.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exception.ClientNotFoundException;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client findById(long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Not found client by id: " + id));
    }

    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }
}
