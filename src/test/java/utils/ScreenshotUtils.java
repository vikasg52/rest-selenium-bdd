package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.file.Paths.*;

public class ScreenshotUtils {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Create the screenshot directory if it doesn't exist
            Path screenshotDir = get("target/screenshots");
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }

            // Capture screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            // Generate a unique file name
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotPath = screenshotDir + "/" + screenshotName + "_" + timestamp + ".png";

            // Save the screenshot
            Files.copy(source.toPath(), get(screenshotPath));
            return screenshotPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}