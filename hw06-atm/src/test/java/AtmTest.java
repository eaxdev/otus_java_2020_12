import hw.Atm;
import hw.Banknote;
import hw.Nominal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AtmTest {

    private Atm atm;

    @BeforeEach
    void setUp() {
        atm = new Atm(
                List.of(
                        new Banknote(5000),
                        new Banknote(2000),
                        new Banknote(2000),
                        new Banknote(1000),
                        new Banknote(1000),
                        new Banknote(500),
                        new Banknote(500),
                        new Banknote(200),
                        new Banknote(50)
                )
        );
    }

    @Test
    void initial() {
        assertThat(atm.getAmountCommon()).isEqualTo(12250);

        assertThat(atm.getAmountPerCell())
                .containsOnly(
                        entry(Nominal.FIVE_THOUSAND, 5000),
                        entry(Nominal.TWO_THOUSAND, 4000),
                        entry(Nominal.THOUSAND, 2000),
                        entry(Nominal.FIVE_HUNDRED, 1000),
                        entry(Nominal.TWO_HUNDRED, 200),
                        entry(Nominal.FIFTY, 50)
                );
    }

    @Test
    void depositMoney() {
        atm.depositMoney(
                List.of(
                        new Banknote(1000),
                        new Banknote(1000),
                        new Banknote(1000),
                        new Banknote(5000)
                )
        );

        assertThat(atm.getAmountCommon()).isEqualTo(20250);

        assertThat(atm.getAmountPerCell())
                .containsOnly(
                        entry(Nominal.FIVE_THOUSAND, 10_000),
                        entry(Nominal.TWO_THOUSAND, 4_000),
                        entry(Nominal.THOUSAND, 5_000),
                        entry(Nominal.FIVE_HUNDRED, 1_000),
                        entry(Nominal.TWO_HUNDRED, 200),
                        entry(Nominal.FIFTY, 50)
                );
    }

    @Test
    void getMoney() {
        List<Banknote> banknotes = atm.getBanknotes(8750);
        assertThat(banknotes).hasSize(6);

        assertThat(banknotes)
                .extracting("nominal", "value")
                .contains(
                        tuple(Nominal.FIVE_THOUSAND, 5_000),
                        tuple(Nominal.TWO_THOUSAND, 2_000),
                        tuple(Nominal.THOUSAND, 1_000),
                        tuple(Nominal.FIVE_HUNDRED, 500),
                        tuple(Nominal.TWO_HUNDRED, 200),
                        tuple(Nominal.FIFTY, 50)
                );

        assertThat(atm.getAmountCommon()).isEqualTo(3_500);

        assertThat(atm.getAmountPerCell())
                .containsOnly(
                        entry(Nominal.TWO_THOUSAND, 2_000),
                        entry(Nominal.THOUSAND, 1_000),
                        entry(Nominal.FIVE_HUNDRED, 500),
                        entry(Nominal.FIVE_THOUSAND, 0),
                        entry(Nominal.TWO_HUNDRED, 0),
                        entry(Nominal.FIFTY, 0)
                );
    }

    @Test
    void notEnoughMoney() {
        assertThatThrownBy( () -> atm.getBanknotes(100_500))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Not enough money");
    }
}
