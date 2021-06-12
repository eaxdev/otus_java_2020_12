package ru.otus.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.DigitResponse;

public class ResponseObserver implements StreamObserver<DigitResponse> {

    private static final Logger log = LoggerFactory.getLogger(ResponseObserver.class);

    private long lastValue = 0;

    @Override
    public void onNext(DigitResponse response) {
        var value = response.getNumber();
        log.info("OnNext value: {}", value);
        setLastValue(value);
    }

    private synchronized void setLastValue(long value) {
        this.lastValue = value;
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
        var last =  this.lastValue;
        this.lastValue = 0;
        return last;
    }
}
