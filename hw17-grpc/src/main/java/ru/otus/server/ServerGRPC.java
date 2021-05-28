package ru.otus.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerGRPC {

    private static final Logger log = LoggerFactory.getLogger(ServerGRPC.class);

    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("......Server started......");

        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new DigitStreamerService())
                .build();
        server.start();


        log.info("......Wait client on port: {}......",  SERVER_PORT);
        server.awaitTermination();

    }
}
