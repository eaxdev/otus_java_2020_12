public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Provide name of test class");
        }
        String className = args[0];

        TestExecutor testExecutor = new TestExecutor(className);
        testExecutor.execute();
        testExecutor.printResult();
    }
}
