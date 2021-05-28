package ru.otus.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.DigitResponse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ResponseObserver implements StreamObserver<DigitResponse> {

    private static final Logger log = LoggerFactory.getLogger(ResponseObserver.class);

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public void onNext(DigitResponse value) {
        var number = value.getNumber();
        log.info("OnNext value: {}", number);
        counter.set(number);
    }

    @Override
    public void onError(Throwable t) {
        log.error("OnError", t);
    }

    @Override
    public void onCompleted() {
        log.info("OnComplete");
    }

    public synchronized long getLastValueAndReset() {
        var last = counter.get();
        counter.set(0);
        return last;
    }
}
