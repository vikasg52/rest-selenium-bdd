package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import utils.ReportGenerator;

@CucumberOptions(
        features = "src/test/resources/features", // Path to your feature files
        glue = "stepdefinitions", // Package where step definitions are located
        plugin = {"json:target/cucumber-reports/cucumber.json"}, // Plugins for reporting
        tags = "@validation" // Run scenarios with both @smoke and @positive tags
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @AfterSuite
    public void generateReport() {
        String jsonFilePath = "target/cucumber-reports/cucumber.json";
        ReportGenerator.generateReport(jsonFilePath);
    }
}