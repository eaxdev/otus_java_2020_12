package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    List<Client> findAll();
}
