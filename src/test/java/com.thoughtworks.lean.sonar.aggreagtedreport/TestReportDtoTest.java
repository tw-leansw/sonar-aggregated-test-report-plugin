package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Lists;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestFrameworkType;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/16/16.
 */
public class TestReportDtoTest extends BaseTest {
    TestReportDto testReport;

    @Before
    public void setUpReport() {
        testReport = new TestReportDto();

        // scenario 1
        TestScenarioDto testScenario1 = new TestScenarioDto()
                .setId(1).setName("s1");
        // 3 passed steps
        for (int i = 0; i < 3; i++) {
            TestStepDto step = new TestStepDto().setId(i).setName("step_in_s1_" + i).setDuration(1000).setResultType(PASSED);
            testScenario1.addStep(step);
        }
        // 1 failed step
        testScenario1.addStep(new TestStepDto().setId(4).setName("meet_problem").setDuration(100).setResultType(FAILED));

        // 2 skipped step
        testScenario1.addStep(new TestStepDto().setId(5).setName("step5").setDuration(100).setResultType(SKIPPED));
        testScenario1.addStep(new TestStepDto().setId(6).setName("step6").setDuration(1000).setResultType(SKIPPED));

        // scenario 2
        TestScenarioDto testScenario2 = new TestScenarioDto()
                .setId(2).setName("s2");

        // also 3 passed steps, but no failed or skipped steps
        for (int i = 0; i < 3; i++) {
            TestStepDto step = new TestStepDto().setId(i).setName("step_in_s2_" + i).setDuration(1000).setResultType(PASSED);
            testScenario2.addStep(step);
        }

        // scenario 3
        TestScenarioDto testScenario3 = new TestScenarioDto()
                .setId(3).setName("s3");

        // 4 steps are all skipped
        for (int j = 0; j < 4; j++) {
            TestStepDto step = new TestStepDto().setId(j).setName("step_in_s3_" + j).setDuration(1000).setResultType(SKIPPED);
            testScenario3.addStep(step);
        }

        // scenario 4 no steps at all
        TestScenarioDto testScenario4 = new TestScenarioDto()
                .setId(4).setName("s4");

        // s1 belongs to unit_test
        // so we have 3 passed , 1 failed and 2 skipped steps in unit_test

        TestFeatureDto testFeatureDto1 =
                new TestFeatureDto()
                        .setTestType(TestType.UNIT_TEST)
                        .setFrameworkType(TestFrameworkType.CUCUMBER)
                        .setCreateTime(new Date())
                        .addScenario(testScenario1);

        // s2, s3, s4 belongs to component_test
        // so we have 3 passed and 4 skipped steps

        TestFeatureDto testFeatureDto2 =
                new TestFeatureDto()
                        .setTestType(TestType.COMPONENT_TEST)
                        .setFrameworkType(TestFrameworkType.JUNIT)
                        .setCreateTime(new Date())
                        .addScenario(testScenario2)
                        .addScenario(testScenario3)
                        .addScenario(testScenario4);

        List<TestFeatureDto> featureDtoList = Lists.newArrayList();
        featureDtoList.add(testFeatureDto1);
        featureDtoList.add(testFeatureDto2);

        testReport.setTestFeatures(featureDtoList);

    }

    @Test
    public void should_get_correct_scenario_number() {
        assertEquals(1, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(3, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
    }

    @Test
    public void should_get_correct_step_number_from_test_report() {
        assertEquals(6, testReport.getStepsByResultType(PASSED).size());
        assertEquals(1, testReport.getStepsByResultType(FAILED).size());
        assertEquals(6, testReport.getStepsByResultType(SKIPPED).size());
    }


}
