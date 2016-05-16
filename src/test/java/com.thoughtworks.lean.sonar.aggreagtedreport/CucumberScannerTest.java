package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.CucumberScanner;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/16/16.
 */
public class CucumberScannerTest {


    @Test
    public void should_return_correct_cucumber_test_report() throws IOException {
        // given
        JXPathMap ctx = new JXPathMap(new ObjectMapper().readValue(getClass().getResourceAsStream("/cucumber_report.json"), Object.class));
        TestReport testReport=new TestReport("test-pipeline-1","202");
        CucumberScanner cucumberAnalyzer=new CucumberScanner(Sets.newHashSet("@api_test"),Sets.newHashSet("@ui_test"));

        cucumberAnalyzer.analyse(ctx, testReport);

        assertEquals(13,testReport.getDetails().get(TestType.UNIT_TEST).size());
        assertEquals(7,testReport.getDetails().get(TestType.COMPONENT_TEST).size());
        assertEquals(3,testReport.getDetails().get(TestType.FUNCTIONAL_TEST).size());
    }
}
