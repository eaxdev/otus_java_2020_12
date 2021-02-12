import ru.IoCContainer;
import ru.Scanner;
import ru.logging.Calculator;
import ru.logging.TestLogging;
import ru.logging.WithoutLog;

public class Hw05 {

    public static void main(String[] args) throws Exception {
        IoCContainer container = new IoCContainer(new Scanner("ru.logging"));

        System.out.println("-".repeat(5) + "Log with interface (dynamic proxy)" + "-".repeat(5));

        Calculator calculator = container.getObject(Calculator.class);

        calculator.calculation(3);
        calculator.calculation(3, 3);
        calculator.calculation(3, 3, "foo");
        calculator.calculation(5, 6, "foo", "bar");
        calculator.someMethod(5, 6, "bar", "buzz");

        System.out.println("-".repeat(40));

        System.out.println("-".repeat(5) + "Log without interface (cglib)" + "-".repeat(5));
        TestLogging testLogging = container.getObject(TestLogging.class);

        testLogging.calculation(3);
        testLogging.calculation(3, 3);
        testLogging.calculation(3, 3, "foo");
        testLogging.calculationWithoutLog(5, 6, "bar");

        System.out.println("-".repeat(40));

        System.out.println("-".repeat(5) + "Without log" + "-".repeat(5));
        WithoutLog withoutLog = container.getObject(WithoutLog.class);

        withoutLog.calculation(3);
        withoutLog.calculation(3, 3);
        withoutLog.calculation(3, 3, "foo");

        System.out.println("-".repeat(40));
    }
}
