package cucumber.Options;

import excel.ExcelReportGenerator;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import listeners.TestNGDBListener;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.io.IOException;
@Listeners(TestNGDBListener.class)
@CucumberOptions(
//        tags="@MerchantSanity",
        features = "src/test/java/features",
        glue = {"stepDefinitions", "hooks"},
        monochrome = true,
        dryRun = false,
        plugin= {"pretty", "summary",
                "html:target/cucumber.html", "json:target/cucumber-reports/cucumber-TestRunner.json",
                "rerun:target/failed_scenarios.txt",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"})

public class TestRunner extends AbstractTestNGCucumberTests {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("JVM shutdown → generating Excel");
            ExcelReportGenerator.generate();
        }));
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}