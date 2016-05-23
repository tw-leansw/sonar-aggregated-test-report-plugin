package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Lists;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/16/16.
 */
public class TestReportDtoTest extends BaseTest {
    public static final String RESULT_TYPE = "resultType";
    public static final String DURATION = "duration";
    TestReportDto testReport;

    @Ignore
    @Before
    public void setUpReport() throws NoSuchFieldException, IllegalAccessException {
        testReport = new TestReportDto();

        // scenario 1
        TestScenarioDto testScenario1 = new TestScenarioDto()
                .setId(1).setName("s1");
        // 3 passed steps
        List<TestStepDto> passedSteps = objectsOfTestStep(3);
        setField(passedSteps, RESULT_TYPE, PASSED);
        setField(passedSteps, DURATION, 1000);


        // 1 failed step
        TestStepDto step4 = new TestStepDto().setDuration(100).setResultType(FAILED);

        // 2 skipped step
        List<TestStepDto> skippedSteps = objectsOfTestStep(2);
        setField(skippedSteps, RESULT_TYPE, SKIPPED);
        setField(skippedSteps, DURATION, 500);

        List<TestStepDto> testSteps = Lists.newArrayList();
        testSteps.addAll(passedSteps);
        testSteps.add(step4);
        testSteps.addAll(skippedSteps);

        testScenario1.setTestSteps(testSteps);

        // scenario 2
        TestScenarioDto testScenario2 = new TestScenarioDto()
                .setId(2).setName("s2");

        // also 3 passed steps, but no failed or skipped steps
        testScenario2.setTestSteps(setField(objectsOfTestStep(3), RESULT_TYPE, PASSED));

        // scenario 3
        TestScenarioDto testScenario3 = new TestScenarioDto()
                .setId(3).setName("s3");

        // 4 steps are all skipped
        testScenario3.setTestSteps(setField(objectsOfTestStep(3), RESULT_TYPE, SKIPPED));

        // s1 belongs to unit_test
        // so we have 3 passed , 1 failed and 2 skipped steps in unit_test

        TestFeatureDto testFeatureDto1 =
                new TestFeatureDto()
                        .setTestType(TestType.UNIT_TEST)
                        .setFrameworkType(TestFrameworkType.CUCUMBER)
                        .setCreateTime(new Date())
                        .setTestScenarios(Lists.newArrayList(testScenario1));

        // s2, s3 belongs to component_test
        // so we have 3 passed and 4 skipped steps

        TestFeatureDto testFeatureDto2 =
                new TestFeatureDto()
                        .setTestType(TestType.COMPONENT_TEST)
                        .setFrameworkType(TestFrameworkType.JUNIT)
                        .setCreateTime(new Date())
                        .setTestScenarios(Lists.newArrayList(testScenario2, testScenario3));

        List<TestFeatureDto> featureDtoList = Lists.newArrayList();
        featureDtoList.add(testFeatureDto1);
        featureDtoList.add(testFeatureDto2);

        testReport.addTestFeatures(featureDtoList);

    }

    @Test
    public void should_get_correct_scenario_number() {
        assertEquals(1, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(2, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
    }

    @Test
    public void should_get_correct_step_number_from_test_report() {
        assertEquals(6, testReport.getStepsByResultType(PASSED).size());
        assertEquals(1, testReport.getStepsByResultType(FAILED).size());
        assertEquals(5, testReport.getStepsByResultType(SKIPPED).size());
    }

    @Test
    public void should_generate_correct_duration() {
        TestFeatureDto unitTestFeature = testReport.getFeaturesByTestType(TestType.UNIT_TEST).get(0);
        assertEquals(4100, unitTestFeature.getDuration());
        assertEquals(4100, unitTestFeature.getScenarioByName("s1").getDuration());

        List<TestStepDto> allsteps = Lists.newArrayList();
        allsteps.addAll(testReport.getStepsByResultType(PASSED));
        allsteps.addAll(testReport.getStepsByResultType(FAILED));
        allsteps.addAll(testReport.getStepsByResultType(SKIPPED));

        int allDuration = sum(
                with(allsteps)
                        .extract(on(TestStepDto.class).getDuration())).intValue();
        assertEquals(allDuration, testReport.getDuration());

    }

    @Test
    public void should_generate_wanted_dtos() throws NoSuchFieldException, IllegalAccessException {
        List<TestStepDto> objs = objectsOfTestStep(3);
        setField(objs, "name", "hehe");
        setField(objs, DURATION, 24);
        setField(objs, RESULT_TYPE, ResultType.SKIPPED);
        for (TestStepDto step : objs) {
            assertEquals("hehe", step.getName());
            assertEquals(24, step.getDuration());
            assertEquals(SKIPPED, step.getResultType());
        }
    }


}
