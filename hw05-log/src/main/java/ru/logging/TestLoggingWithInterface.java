package ru.logging;

import ru.metadata.Log;

public class TestLoggingWithInterface implements Calculator {

    @Override
    public void calculation(int param1) {
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {

    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {

    }

    @Override
    public void calculationWithoutLog(int param1, int param2, String param3) {
        System.out.println("calculationWithoutLog");
    }
}
