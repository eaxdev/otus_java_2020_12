package ru.otus;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        var generator1 = new DigitGenerator(10, 1);
        var generator2 = new DigitGenerator(10, 1);

        while (true) {
            System.out.println(generator1.generateNext());
            Thread.sleep(1000);
        }
    }

}
