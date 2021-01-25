import java.util.concurrent.atomic.AtomicInteger;

public class TestsResult {

    private final int allTestCount;
    private final AtomicInteger successTests = new AtomicInteger(0);
    private final AtomicInteger failTests = new AtomicInteger(0);

    public TestsResult(int allTestCount) {
        this.allTestCount = allTestCount;
    }

    public int getAllTestCount() {
        return allTestCount;
    }

    public AtomicInteger getSuccessTests() {
        return successTests;
    }

    public AtomicInteger getFailTests() {
        return failTests;
    }

    public void incrementSuccess() {
        successTests.incrementAndGet();
    }

    public void incrementFail() {
        failTests.incrementAndGet();
    }
}
