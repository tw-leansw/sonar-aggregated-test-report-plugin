package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.List;

import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by qmxie on 5/19/16.
 */
public class ReportServiceTest extends BaseTest {

    @Test
    public void should_report_service_work() {
        TestReportService service = new TestReportService(dbClient);

        TestReportDto testReportDto = getRandomTestReportDto(3, 5, 10);

        TestReportDto reportDto2 = getRandomTestReportDto(2, 3, 5);

        service.save(testReportDto);
        service.save(reportDto2);
        TestReportDto ret = service.getReport(testReportDto.getProjectId());
        assertNotNull(ret);
        assertEquals(3, ret.getTestFeatures().size());

        assertEquals(testReportDto.getProjectId(), ret.getProjectId());

        assertEquals(testReportDto.getScenariosNumber(TestType.UNIT_TEST), ret.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(testReportDto.getScenariosNumber(TestType.COMPONENT_TEST), ret.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(testReportDto.getScenariosNumber(TestType.FUNCTIONAL_TEST), ret.getScenariosNumber(TestType.FUNCTIONAL_TEST));

        assertEquals(testReportDto.getStepsByResultType(PASSED).size(), ret.getStepsByResultType(PASSED).size());
        assertEquals(testReportDto.getStepsByResultType(FAILED).size(), ret.getStepsByResultType(FAILED).size());
        assertEquals(testReportDto.getStepsByResultType(SKIPPED).size(), ret.getStepsByResultType(SKIPPED).size());

    }

    @Test
    public void should_many_insert_work() {
        TestReportService service = new TestReportService(dbClient);
        for (int i = 0; i < 30; i++) {
            TestReportDto testReportDto = getRandomTestReportDto(RandomUtils.nextInt(3), RandomUtils.nextInt(3), RandomUtils.nextInt(3));
            String randomString = enhancedRandom.nextObject(String.class);
            testReportDto.setProjectId(randomString);
            service.save(testReportDto);
            TestReportDto ret = service.getReport(randomString);
            assertEquals(testReportDto.toJson(), ret.toJson());
        }
    }

    private TestReportDto getRandomTestReportDto(int featureNumber, int scenarioNumber, int stepNumber) {
        TestReportDto testReportDto = enhancedRandom.nextObject(TestReportDto.class, "testFeatures");
        List<TestFeatureDto> features = objectsOfTestFeature(featureNumber);
        for (TestFeatureDto testFeatureDto : features) {
            List<TestScenarioDto> scenarios = objectsOfTestScenario(scenarioNumber);
            for (TestScenarioDto testScenarioDto : scenarios) {
                // note: due to calculatingPropsFromChildren() is called in setXXXs()
                // these set methods should be called at tail position
                testScenarioDto.setTestSteps(objectsOfTestStep(stepNumber));
            }
            testFeatureDto.setTestScenarios(scenarios);
        }
        testReportDto.addTestFeatures(features);
        return testReportDto;
    }

}
