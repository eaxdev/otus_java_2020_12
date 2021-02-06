package ru.logging;


import ru.metadata.Log;

public interface Calculator {

    @Log
    void calculation(int param1);

    void calculation(int param1, int param2);

    void calculation(int param1, int param2, String param3);

    void calculationWithoutLog(int param1, int param2, String param3);

}
