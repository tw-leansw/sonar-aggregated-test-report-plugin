package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.CucumberScanner;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import org.junit.Test;

import java.io.IOException;

import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/16/16.
 */
public class CucumberScannerTest {


    @Test
    public void should_return_correct_cucumber_test_report() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report.json"), Object.class));
        TestReport testReport = new TestReport("test-pipeline-1", "202");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(13, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(7, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(3, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
    }

    @Test
    public void should_return_correct_cucumber_test_report_2() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report_2.json"), Object.class));
        TestReport testReport = new TestReport("test-pipeline-1", "202");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(0, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(3, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
    }

    @Test
    public void should_return_correct_cucumber_test_report_3() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report_3.json"), Object.class));
        TestReport testReport = new TestReport("test-pipeline-3", "203");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(5, testReport.getStepsByResultType(PASSED).size());
        assertEquals(1, testReport.getStepsByResultType(FAILED).size());
        assertEquals(2, testReport.getStepsByResultType(SKIPPED).size());
    }
}
