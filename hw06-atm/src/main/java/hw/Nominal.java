package hw;

import java.util.Arrays;

public enum Nominal {

    FIVE(5),

    TEN(10),

    FIFTY(50),

    HUNDRED(100),

    TWO_HUNDRED(200),

    FIVE_HUNDRED(500),

    THOUSAND(1000),

    TWO_THOUSAND(2000),

    FIVE_THOUSAND(5000);

    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    public static Nominal of(int value) {
        return Arrays.stream(Nominal.values())
                .filter(it -> it.value == value)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not supported nominal value: " + value));
    }

    public int getValue() {
        return value;
    }
}
