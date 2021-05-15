package ru.otus;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DigitGenerator {

    private final int threshold;

    private final int initial;

    private final AtomicInteger counter;

    private final AtomicBoolean isReverse = new AtomicBoolean(false);

    public DigitGenerator(int threshold, int initial) {
        this.threshold = threshold;
        this.initial = initial;
        this.counter = new AtomicInteger(initial);
    }

    public int generateNext() {
        if (counter.get() == threshold) {
            isReverse.set(true);
        }

        if (counter.get() == initial) {
            isReverse.set(false);
        }

        if (isReverse.get()) {
            return counter.getAndDecrement();
        }

        return counter.getAndIncrement();
    }

}
