public class ToSystemOutPrinter implements ResultPrinter {

    private final TestsResult testsResult;

    public ToSystemOutPrinter(TestsResult testsResult) {
        this.testsResult = testsResult;
    }

    public void print() {
        System.out.printf("""
                All tests: %s
                Success tests: %s
                Fail tests: %s
                %n""", testsResult.getAllTestCount(), testsResult.getSuccessTests(), testsResult.getFailTests());
    }
}
