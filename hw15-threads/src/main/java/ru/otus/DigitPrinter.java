package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigitPrinter {

    private static final Logger logger = LoggerFactory.getLogger(DigitPrinter.class);

    private String lastThreadName;

    public DigitPrinter(String lastThreadName) {
        this.lastThreadName = lastThreadName;
    }

    public synchronized void print(DigitGenerator generator) {
        var threadName = Thread.currentThread().getName();
        try {
            while (!Thread.currentThread().isInterrupted()) {

                while (lastThreadName.equals(threadName)) {
                    this.wait();
                }

                lastThreadName = threadName;
                logger.debug("Thread: {}, print: {}", threadName, generator.generateNext());
                Thread.sleep(1000);
                notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
