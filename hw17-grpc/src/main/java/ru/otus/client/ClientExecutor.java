package ru.otus.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.DigitRequest;
import ru.otus.RemoteDigitServiceGrpc;

import java.util.concurrent.TimeUnit;

public class ClientExecutor {

    private static final Logger log = LoggerFactory.getLogger(ClientExecutor.class);

    private final RemoteDigitServiceGrpc.RemoteDigitServiceStub asyncClient;

    private long counter = 0;

    public ClientExecutor(RemoteDigitServiceGrpc.RemoteDigitServiceStub asyncClient) {
        this.asyncClient = asyncClient;
    }

    public void execute() throws InterruptedException {
        var request = createRequest();
        var observer = new ResponseObserver();
        asyncClient.digit(request, observer);

        for (var i = 0; i < 50; i++) {
            var nextValue = getNextValue(observer);
            log.info("Current value: {}", nextValue);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private long getNextValue(ResponseObserver observer) {
        counter = counter + observer.getLastValueAndReset() + 1;
        return counter;
    }

    private DigitRequest createRequest() {
        return DigitRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
    }
}
