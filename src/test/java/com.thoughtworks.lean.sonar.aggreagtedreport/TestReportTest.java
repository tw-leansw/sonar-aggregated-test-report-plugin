package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import org.junit.Before;
import org.junit.Test;

import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/16/16.
 */
public class TestReportTest {
    TestReport testReport;

    @Before
    public void setUp() {
        testReport = new TestReport();

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
        testReport.addScenario(TestType.UNIT_TEST, testScenario1);

        // s2, s3, s4 belongs to component_test
        // so we have 3 passed and 4 skipped steps
        testReport.addScenario(TestType.COMPONENT_TEST, testScenario2);
        testReport.addScenario(TestType.COMPONENT_TEST, testScenario3);
        testReport.addScenario(TestType.COMPONENT_TEST, testScenario4);

    }

    @Test
    public void should_get_correct_scenario_number() {
        assertEquals(1, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(3, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
    }

    @Test
    public void should_get_correct_step_number_from_scenario() {
        assertEquals(3, testReport.getScenarios(TestType.UNIT_TEST).get(0).getStepsByResultType(PASSED).size());
        assertEquals(1, testReport.getScenarios(TestType.UNIT_TEST).get(0).getStepsByResultType(FAILED).size());
        assertEquals(2, testReport.getScenarios(TestType.UNIT_TEST).get(0).getStepsByResultType(SKIPPED).size());

        assertEquals(0, testReport.getScenarios(TestType.COMPONENT_TEST).get(0).getStepsByResultType(FAILED).size());
        assertEquals(0, testReport.getScenarios(TestType.COMPONENT_TEST).get(0).getStepsByResultType(SKIPPED).size());
    }

    @Test
    public void should_get_correct_step_number_from_test_report() {
        System.out.println("");
        assertEquals(6, testReport.getStepsByResultType(PASSED).size());
        assertEquals(1, testReport.getStepsByResultType(FAILED).size());
        assertEquals(6, testReport.getStepsByResultType(SKIPPED).size());
    }

}
