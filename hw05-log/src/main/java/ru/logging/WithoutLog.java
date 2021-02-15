package ru.logging;

import ru.metadata.ProxyStrategy;

@ProxyStrategy(ProxyStrategy.Strategy.CGLIB)
public class WithoutLog {

    public void calculation(int param1) {

    }

    public void calculation(int param1, int param2) {

    }

    public void calculation(int param1, int param2, String param3) {

    }

}
