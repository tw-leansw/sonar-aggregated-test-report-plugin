package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.CucumberScanner;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import org.junit.Test;

import java.io.IOException;

import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;
import static org.junit.Assert.*;

/**
 * Created by qmxie on 5/16/16.
 */
public class CucumberScannerTest extends BaseTest {

    @Test
    public void should_return_correct_cucumber_test_report() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report.json"), Object.class));
        TestReportDto testReport = new TestReportDto().setProjectId("test-pipeline-3").setBuildLabel("203");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(13, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(7, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(3, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
        //
        assertTrue(testReport.getDuration() > 0);
    }

    @Test
    public void should_return_correct_cucumber_test_report_2() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report_2.json"), Object.class));
        TestReportDto testReport = new TestReportDto().setProjectId("test-pipeline-3").setBuildLabel("203");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(0, testReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(3, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));

        //
        assertTrue(testReport.getDuration() > 0);
    }

    @Test
    public void should_return_correct_cucumber_test_report_3() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report_3.json"), Object.class));
        TestReportDto testReport = new TestReportDto().setProjectId("test-pipeline-3").setBuildLabel("203");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(5, testReport.getStepsByResultType(PASSED).size());
        assertEquals(1, testReport.getStepsByResultType(FAILED).size());
        assertEquals(2, testReport.getStepsByResultType(SKIPPED).size());

        assertTrue(testReport.getDuration() > 0);
    }

    @Test
    public void should_get_same_test_report_after_save_and_get() throws IOException {
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report_3.json"), Object.class));
        TestReportDto testReport = new TestReportDto().setProjectId("test-pipeline-3").setBuildLabel("203");
        CucumberScanner cucumberAnalyzer = new CucumberScanner(Sets.newHashSet("@api_test"), Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        TestReportService service = new TestReportService(dbClient);
        service.save(testReport);

        TestReportDto ret = service.getReport("test-pipeline-3");

        assertEquals(testReport, ret);
        assertNotSame(testReport, ret);
        assertEquals(testReport.toJson(), ret.toJson());
        System.out.println(testReport.toJson());
    }
}
