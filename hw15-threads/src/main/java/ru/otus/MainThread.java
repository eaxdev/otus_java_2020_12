package ru.otus;

public class MainThread {

    private static final String FIRST_THREAD = "Thread1";
    private static final String SECOND_THREAD = "Thread2";

    public static void main(String[] args) {
        var generator1 = new DigitGenerator(10, 1);
        var generator2 = new DigitGenerator(10, 1);


        var printer = new DigitPrinter(SECOND_THREAD);
        new Thread(() -> printer.print(generator1), FIRST_THREAD).start();
        new Thread(() -> printer.print(generator2), SECOND_THREAD).start();

    }

}
