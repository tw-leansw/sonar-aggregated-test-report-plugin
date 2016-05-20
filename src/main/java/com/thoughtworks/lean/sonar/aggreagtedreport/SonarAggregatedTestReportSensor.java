package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.exception.LeanPluginException;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.ReportScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;


public class SonarAggregatedTestReportSensor implements Sensor {

    private Settings settings;
    private FileSystem fileSystem;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());


    public SonarAggregatedTestReportSensor(FileSystem fileSystem, Settings settings) {
        this.fileSystem = fileSystem;
        this.settings = settings;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        try {
            ReportScanner scanner = new ReportScanner(this.settings, this.fileSystem);
            scanner.scanReport(project);
        } catch (LeanPluginException e) {
            LOGGER.warn("Make sure add -Dlean.aggregated.test.project.build is set.");
            return;
        }
    }

    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

}