package com.thoughtworks.lean.sonar.aggreagtedreport.service;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.exception.LeanPluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.db.DefaultDatabase;

import java.util.List;

/**
 * Created by qmxie on 5/19/16.
 */
public class TestReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestReportService.class);

    private MyDbClient dbClient;

    public TestReportService(MyDbClient dbClient) {
        this.dbClient = dbClient;
    }

    public TestReportService(Settings settings) {
        DefaultDatabase defaultDatabase = new DefaultDatabase(settings);
        defaultDatabase.start();

        Mybatis mybatis = new Mybatis(defaultDatabase);
        dbClient = new MyDbClient(mybatis);
        mybatis.start();
    }

    public void save(TestReportDto testReport) {
        dbClient.getTestReportDao().insert(testReport);
        testReport.setChildrenzParentId();
        dbClient.getTestFeatureDao().insert(testReport.getTestFeatures());
        for (TestFeatureDto feature : testReport.getTestFeatures()) {
            feature.setChildrenzParentId();
            dbClient.getTestScenarioDao().insert(feature.getTestScenarios());
            for (TestScenarioDto scenario : feature.getTestScenarios()) {
                scenario.setChildrenzParentId();
                dbClient.getTestStepDao().insert(scenario.getTestSteps());
            }
        }
    }


    public TestReportDto getReport(String projectId) {
        TestReportDto report = dbClient.getTestReportDao().getLatestByProjectId(projectId);
        if (report == null) {
            LOGGER.debug("No report found for project ID: " + projectId);
            throw new LeanPluginException("No report found for project ID: " + projectId);
        }
        List<TestFeatureDto> features = dbClient.getTestFeatureDao().getByParentId(report.getId());
        report.addTestFeatures(features);
        for (TestFeatureDto feature : features) {
            feature.setTestScenarios(dbClient.getTestScenarioDao().getByParentId(feature.getId()));
            for (TestScenarioDto scenraio : feature.getTestScenarios()) {
                scenraio.setTestSteps(dbClient.getTestStepDao().getByParentId(scenraio.getId()));
            }
        }
        return report;
    }
}
