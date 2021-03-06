package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import com.google.common.base.Strings;
import com.thoughtworks.lean.sonar.aggreagtedreport.Constants;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.exception.LeanPluginException;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class ReportScanner {
    private CucumberScanner cucumberScanner;
    private GaugeScanner gaugeScanner;
    private JUnitScanner junitScanner;
    private TestReportDto report = new TestReportDto();
    private TestReportService reportService;
    private String buildLabel;
    private Logger LOGGER = Loggers.get(getClass());

    public ReportScanner(Settings settings, FileSystem projectFileSystem) {
        buildLabel = settings.getString(Constants.LEAN_AGGREGATED_TEST_PROJECT_BUILD);
        if (Strings.isNullOrEmpty(buildLabel)) {
            throw new LeanPluginException("Make sure add -Dlean.aggregated.test.project.build is set.");
        }
        cucumberScanner = new CucumberScanner(settings, projectFileSystem);
        gaugeScanner = new GaugeScanner(settings, projectFileSystem);
        junitScanner = new JUnitScanner(settings, projectFileSystem);
        reportService = new TestReportService(settings);
    }

    public void scanReport(Project project) {
        this.report.setProjectId(project.getKey());
        this.report.setBuildLabel(this.buildLabel);
        LOGGER.debug("scan Report for project: " + project.getKey() + " ,build: " + this.buildLabel);

        cucumberScanner.analyse(report);
        gaugeScanner.analyse(report);
        junitScanner.analyse(report);

        reportService.save(this.report);
    }


}
