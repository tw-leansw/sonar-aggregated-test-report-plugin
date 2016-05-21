package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.GaugeScanner;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.ScriptUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by qmxie on 5/16/16.
 */
public class GaugeScannerTest {
    @Test
    public void should_return_1_functional_test() throws IOException {

        // given
        String jsString = IOUtils.toString(getClass().getResourceAsStream("/gauge_report.js"));
        JXPathMap ctx = ScriptUtil.eval(jsString);
        TestReportDto testReport = new TestReportDto();
        GaugeScanner gaugeScanner = new GaugeScanner(Sets.newHashSet("api_test"), Sets.newHashSet("ui_test"));

        // when

        gaugeScanner.analyse(ctx, testReport);
        // then
        assertEquals(1, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.UNIT_TEST));

        //
        assertTrue(testReport.getDuration() > 0);


    }

    @Test
    public void should_return_correct_functional_test() throws IOException {

        // given
        String jsString = IOUtils.toString(getClass().getResourceAsStream("/gauge_report_2.js"));
        JXPathMap ctx = ScriptUtil.eval(jsString);
        TestReportDto testReport = new TestReportDto();
        GaugeScanner gaugeScanner = new GaugeScanner(Sets.newHashSet("api_test"), Sets.newHashSet("ui_test"));

        // when

        gaugeScanner.analyse(ctx, testReport);
        // then
        assertEquals(0, testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
        assertEquals(0, testReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(1, testReport.getScenariosNumber(TestType.UNIT_TEST));

        //
        assertTrue(testReport.getDuration() > 0);

    }

    @Test
    public void should_return_correct_failed_scenarios() throws IOException {
        // given
        String jsString = IOUtils.toString(getClass().getResourceAsStream("/gauge_report_with_failed.js"));
        JXPathMap ctx = ScriptUtil.eval(jsString);
        TestReportDto testReport = new TestReportDto();
        GaugeScanner gaugeScanner = new GaugeScanner(Sets.newHashSet("api_test"), Sets.newHashSet("ui_test"));

        // when
        gaugeScanner.analyse(ctx, testReport);
        // then
        assertEquals(2, testReport.getStepsByResultType(ResultType.FAILED).size());
        assertEquals(16, testReport.getStepsByResultType(ResultType.PASSED).size());
        assertEquals(0, testReport.getStepsByResultType(ResultType.SKIPPED).size());

        assertEquals(2, testReport.getScenariosByResultType(ResultType.FAILED).size());
        assertEquals(14, testReport.getScenariosByResultType(ResultType.PASSED).size());
        assertEquals(0, testReport.getScenariosByResultType(ResultType.SKIPPED).size());
        //
        assertTrue(testReport.getDuration() > 0);
    }

}
