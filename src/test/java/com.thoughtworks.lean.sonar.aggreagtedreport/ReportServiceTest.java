package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import org.junit.Assert;
import org.junit.Test;

import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by qmxie on 5/19/16.
 */
public class ReportServiceTest extends BaseTest {

    @Test
    public void should_report_service_work() {
        TestReportService service = new TestReportService(dbClient);

        TestReportDto testReportDto = getTestReportDto();

        TestReportDto reportDto2 = getTestReportDto();

        service.save(testReportDto);
        service.save(reportDto2);
        TestReportDto ret = service.getReport(testReportDto.getProjectId());
        assertNotNull(ret);
        assertEquals(3, ret.getTestFeatures().size());

        Assert.assertEquals(testReportDto.getProjectId(), ret.getProjectId());

        Assert.assertEquals(testReportDto.getScenariosNumber(TestType.UNIT_TEST), ret.getScenariosNumber(TestType.UNIT_TEST));
        Assert.assertEquals(testReportDto.getScenariosNumber(TestType.COMPONENT_TEST), ret.getScenariosNumber(TestType.COMPONENT_TEST));
        Assert.assertEquals(testReportDto.getScenariosNumber(TestType.FUNCTIONAL_TEST), ret.getScenariosNumber(TestType.FUNCTIONAL_TEST));

        Assert.assertEquals(testReportDto.getStepsByResultType(PASSED).size(), ret.getStepsByResultType(PASSED).size());
        Assert.assertEquals(testReportDto.getStepsByResultType(FAILED).size(), ret.getStepsByResultType(FAILED).size());
        Assert.assertEquals(testReportDto.getStepsByResultType(SKIPPED).size(), ret.getStepsByResultType(SKIPPED).size());

    }

    private TestReportDto getTestReportDto() {
        TestReportDto testReportDto = enhancedRandom.nextObject(TestReportDto.class, "testFeatures");
        //
        testReportDto.setTestFeatures(objects(TestFeatureDto.class, 3, "testScenarios"));
        for (TestFeatureDto testFeatureDto : testReportDto.getTestFeatures()) {
            testFeatureDto.setTestScenarios(objects(TestScenarioDto.class, 5, "testSteps"));
            for (TestScenarioDto testScenarioDto : testFeatureDto.getTestScenarios()) {
                testScenarioDto.setTestSteps(objects(TestStepDto.class, 10));
            }
        }
        return testReportDto;
    }

}
