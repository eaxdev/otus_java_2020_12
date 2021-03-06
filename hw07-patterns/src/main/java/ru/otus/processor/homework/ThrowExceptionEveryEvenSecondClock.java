package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.Clock;
import java.time.LocalTime;

public class ThrowExceptionEveryEvenSecondClock implements Processor {

    private final Clock clock;

    public ThrowExceptionEveryEvenSecondClock(Clock clock) {
        this.clock = clock;
    }

    public ThrowExceptionEveryEvenSecondClock() {
        this(Clock.systemDefaultZone());
    }

    @Override
    public Message process(Message message) {
        int currentSecond = LocalTime.now(clock).getSecond();
        if (currentSecond % 2 == 0) {
            throw new EvenSecondException(currentSecond);
        }
        return message.toBuilder().build();
    }
}
