package utils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReportGenerator {
    public static void generateReport(String jsonFilePath) {
        File reportOutputDirectory = new File(System.getProperty("report.dir", "target/cucumber-reports"));
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add(jsonFilePath);

        String buildNumber = System.getProperty("build.number", "1");
        String projectName = System.getProperty("project.name", "Restful Booker Automation");

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.setBuildNumber(buildNumber);
        configuration.addClassifications("Platform", System.getProperty("os.name"));
        configuration.addClassifications("Browser", System.getProperty("browser", "Chrome"));
        configuration.addClassifications("Branch", System.getProperty("branch", "master"));

//        configuration.setPresentationModes(List.of(PresentationMode.RUN_WITH_JENKINS));
//        configuration.setNotFailingStatuses(Set.of(Status.SKIPPED));

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }
}
