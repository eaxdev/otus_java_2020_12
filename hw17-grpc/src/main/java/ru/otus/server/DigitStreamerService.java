package ru.otus.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.DigitRequest;
import ru.otus.DigitResponse;
import ru.otus.RemoteDigitServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DigitStreamerService extends RemoteDigitServiceGrpc.RemoteDigitServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(DigitStreamerService.class);

    @Override
    public void digit(DigitRequest request, StreamObserver<DigitResponse> responseObserver) {
        log.info("Start streaming....Request: {}", request);
        var executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(createTask(request, executor, responseObserver), 0, 2, TimeUnit.SECONDS);
    }

    private Runnable createTask(DigitRequest request, ScheduledExecutorService executor, StreamObserver<DigitResponse> responseObserver) {
        var currentValue = new AtomicLong(request.getFirstValue());
        return () -> {
            var newValue = currentValue.incrementAndGet();
            var response = DigitResponse.newBuilder()
                    .setNumber(newValue)
                    .build();
            responseObserver.onNext(response);
            if (newValue == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("Finish streaming......");
            }
        };
    }
}
