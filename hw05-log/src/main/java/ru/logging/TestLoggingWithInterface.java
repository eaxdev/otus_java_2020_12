package ru.logging;

import ru.metadata.Log;
import ru.metadata.ProxyStrategy;

@ProxyStrategy(ProxyStrategy.Strategy.DYNAMIC_PROXY)
public class TestLoggingWithInterface implements Calculator {

    @Log
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
    public void calculation(int param1, int param2, String param3, String param4) {
        System.out.println("calculationWithoutLog");
    }

    @Override
    public void someMethod(int param1, int param2, String param3, String param4) {
        System.out.println("someMethodWithoutLog");
    }
}
