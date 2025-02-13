package utils;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.ScreenshotUtils;

public class Hooks {
    private WebDriver driver; // Initialize your WebDriver

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            // Capture screenshot on failure
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, scenario.getName());
            System.out.println("Screenshot captured: " + screenshotPath);

            // Attach screenshot to Cucumber report
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}