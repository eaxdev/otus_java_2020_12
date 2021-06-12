package ru.otus.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.RemoteDigitServiceGrpc;

public class ClientGRPC {

    private static final Logger log = LoggerFactory.getLogger(ClientGRPC.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        log.info("Client started.......");
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteDigitServiceGrpc.newStub(channel);

        new ClientExecutor(stub).execute();

        log.info("Client finished.......");
        channel.shutdown();
    }

}
