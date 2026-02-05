package listeners;

import db.ScenarioRepository;
import db.TestResultRepository;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.TestContext;

public class TestNGDBListener implements ITestListener {
    @Override
    public void onTestSuccess(ITestResult result) {
        persist("PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        persist("FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        persist("SKIP");
    }

    private void persist(String status) {
        TestContext context = TestContext.get();
        context.setEndTime(System.currentTimeMillis());
        ScenarioRepository.finishScenario(context, status);        TestContext.clear();
    }

}
