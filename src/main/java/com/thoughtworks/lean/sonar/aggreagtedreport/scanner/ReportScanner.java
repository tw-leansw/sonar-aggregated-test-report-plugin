package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

/**
 * Created by qmxie on 5/20/16.
 */
public class ReportScanner {
    private CucumberScanner cucumberScanner;
    private GaugeScanner gaugeScanner;
    //JunitScanner junitScanner;
    private TestReportDto report = new TestReportDto();
    private TestReportService reportService;
    private String buildLabel;

    public ReportScanner(Settings settings, FileSystem projectFileSystem) {
        cucumberScanner = new CucumberScanner(settings, projectFileSystem);
        gaugeScanner = new GaugeScanner(settings, projectFileSystem);
        reportService = new TestReportService(settings);
        buildLabel = settings.getString("lean.aggregated.test.project.build");
    }

    public void scanReport(Project project){
        this.report.setProjectId(project.getKey());
        this.report.setBuildLabel(this.buildLabel);
        cucumberScanner.analyse(report);
        //gaugeScanner.analyse(report);
        reportService.save(this.report);
    }


}
