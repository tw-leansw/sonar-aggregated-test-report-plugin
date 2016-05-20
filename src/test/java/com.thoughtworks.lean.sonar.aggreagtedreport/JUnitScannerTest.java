package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.JUnitScanner;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by qmxie on 5/20/16.
 */
public class JUnitScannerTest {
    @Test
    public void should_analyze_junit_report_success() {

        JUnitScanner jUnitScanner = new JUnitScanner(
                Sets.newHashSet("^IT.*$", "^API_.*$"),
                Sets.newHashSet("^FT.*$", "^UI_*$"))
                .setReportPath("/surefire-reports");
        TestReportDto testsReport = new TestReportDto();
        jUnitScanner.analyse(testsReport);

        // then
        assertEquals(18, testsReport.getScenariosNumber(TestType.UNIT_TEST));
        assertEquals(4, testsReport.getScenariosNumber(TestType.COMPONENT_TEST));
        assertEquals(4, testsReport.getScenariosNumber(TestType.FUNCTIONAL_TEST));
    }
}
