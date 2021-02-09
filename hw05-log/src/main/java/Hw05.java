import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.ProxyCreator;
import ru.logging.Calculator;
import ru.logging.TestLogging;
import ru.logging.TestLoggingWithInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Hw05 {

    private static final Map<Class<?>, Object> CACHE = new HashMap<>();

    public static void main(String[] args) {
        Reflections scanner = new Reflections("ru.logging", new SubTypesScanner(false));
        Set<Class<?>> allClasses = scanner.getSubTypesOf(Object.class)
                .stream().filter(it -> !it.isInterface() || !it.isAnnotation()).collect(Collectors.toSet());

        ProxyCreator proxyCreator = new ProxyCreator();

        for (Class<?> clazz : allClasses) {
            Object obj = proxyCreator.createProxyIfNeed(clazz);
            CACHE.put(clazz, obj);
        }

        System.out.println("-".repeat(5) + "Log with interface (dynamic proxy)" + "-".repeat(5));
        Calculator calculator = (Calculator) CACHE.get(TestLoggingWithInterface.class);

        calculator.calculation(3);
        calculator.calculation(3, 3);
        calculator.calculation(3, 3, "foo");
        calculator.calculation(5, 6, "bar");
        calculator.someMethod(5, 6, "bar", "buzz");
        calculator.calculation(5, 6, "foo", "bar");

        System.out.println("-".repeat(40));

        System.out.println("-".repeat(5) + "Log without interface (cglib)" + "-".repeat(5));
        TestLogging testLogging = (TestLogging) CACHE.get(TestLogging.class);

        testLogging.calculation(3);
        testLogging.calculation(3, 3);
        testLogging.calculation(3, 3, "foo");
        testLogging.calculationWithoutLog(5, 6, "bar");

        System.out.println("-".repeat(40));
    }
}
