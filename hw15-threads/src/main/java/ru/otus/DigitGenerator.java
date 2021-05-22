package ru.otus;

public class DigitGenerator {

    private final int threshold;

    private final int initial;

    private int counter;

    private boolean isReverse = false;

    public DigitGenerator(int threshold, int initial) {
        this.threshold = threshold;
        this.initial = initial;
        this.counter = initial;
    }

    public int generateNext() {
        if (counter == threshold) {
            isReverse = true;
        }

        if (counter == initial) {
            isReverse = false;
        }

        if (isReverse) {
            return counter--;
        }

        return counter++;
    }

}
