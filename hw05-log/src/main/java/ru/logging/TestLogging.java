package ru.logging;

import ru.metadata.Log;
import ru.metadata.ProxyStrategy;

@ProxyStrategy(ProxyStrategy.Strategy.CGLIB)
public class TestLogging {

    @Log
    public void calculation(int param1) {

    }

    @Log
    public void calculation(int param1, int param2) {

    }

    @Log
    public void calculation(int param1, int param2, String param3) {

    }

    public void calculationWithoutLog(int param1, int param2, String param3) {
        System.out.println("calculationWithoutLog");
    }
}
