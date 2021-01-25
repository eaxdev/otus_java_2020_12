import metadata.After;
import metadata.Before;
import metadata.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestExecutor {

    private final Class<?> testClass;

    private final TestsResult testsResult;

    private final List<Method> beforeMethods;

    private final List<Method> afterMethods;

    private final List<Method> testMethods;

    private final ResultPrinter resultPrinter;

    public TestExecutor(String classWithTests) throws ClassNotFoundException {
        testClass = Class.forName(classWithTests);

        Method[] methods = testClass.getDeclaredMethods();

        beforeMethods = Arrays.stream(methods)
                .filter(it -> it.getAnnotation(Before.class) != null)
                .collect(Collectors.toList());

        afterMethods = Arrays.stream(methods)
                .filter(it -> it.getAnnotation(After.class) != null)
                .collect(Collectors.toList());

        testMethods = Arrays.stream(methods)
                .filter(it -> it.getAnnotation(Test.class) != null)
                .collect(Collectors.toList());

        testsResult = new TestsResult(testMethods.size());
        resultPrinter = new ToSystemOutPrinter(testsResult);
    }

    public void execute() throws Exception {
        for (Method testMethod : testMethods) {
            invokeTest(testClass, beforeMethods, afterMethods, testMethod, testsResult);
        }
    }

    public void printResult() {
        resultPrinter.print();
    }

    private void invokeTest(Class<?> testClass, List<Method> beforeMethods,
                            List<Method> afterMethods, Method testMethod, TestsResult testsResult) throws Exception {
        Object instance = testClass.getDeclaredConstructor().newInstance();

        try {
            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(instance);
            }

            testMethod.invoke(instance);

            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(instance);
            }
            testsResult.incrementSuccess();
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable targetException;
            if (e instanceof InvocationTargetException invocationTargetException) {
                targetException = invocationTargetException.getTargetException();

                if (targetException instanceof AssertionError assertionError) {
                    System.err.println(assertionError.getMessage());
                }
            }
            testsResult.incrementFail();
        }
    }

}