package ru.otus.services;


import ru.otus.hibernate.service.DBServiceClient;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DBServiceClient dbServiceClient;

    public ClientAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
