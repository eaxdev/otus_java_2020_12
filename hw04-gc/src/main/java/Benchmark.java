import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {

    private final List<SomeClass> OBJECTS = new ArrayList<>();

    public void run() {
        while (true) {
            for (int i = 0; i < 100_000; i++) {
                OBJECTS.add(new SomeClass(i, String.valueOf(i), Long.valueOf(i), Character.valueOf((char) i), true, (short) 127));
            }
            for (int i = 0; i < 1_000; i++) {
                OBJECTS.remove(i);
            }
        }
    }

    @Override
    public int getSize() {
        return OBJECTS.size();
    }

    private static class SomeClass {
        private final Integer i1;
        private final String s1;
        private final Long l1;
        private final Character c1;
        private final Boolean b1;
        private final Short sh1;

        private SomeClass(Integer i1, String s1, Long l1, Character c1, Boolean b1, Short sh1) {
            this.i1 = i1;
            this.s1 = s1;
            this.l1 = l1;
            this.c1 = c1;
            this.b1 = b1;
            this.sh1 = sh1;
        }
    }
}
