package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.ReportScanner;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;


public class SonarAggregatedTestReportSensor implements Sensor {

    private Settings settings;
    private FileSystem fileSystem;


    public SonarAggregatedTestReportSensor(FileSystem fileSystem, Settings settings) {
        this.fileSystem = fileSystem;
        this.settings = settings;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        ReportScanner scanner = new ReportScanner(this.settings,this.fileSystem);
        scanner.scanReport(project);
    }

    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

}