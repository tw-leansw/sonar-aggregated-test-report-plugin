package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.scanner.GaugeScanner;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.ScriptUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by qmxie on 5/16/16.
 */
public class GaugeScannerTest {
    @Ignore
    @Test
    public void should_return_1_functional_test() throws IOException {

        // given
        String jsString = IOUtils.toString(getClass().getResourceAsStream("/gauge_report.js"));
        JXPathMap ctx = ScriptUtil.eval(jsString);
        TestReport testReport = new TestReport();
        GaugeScanner gaugeScanner=new GaugeScanner(Sets.newHashSet("api_test"),Sets.newHashSet("ui_test"));

        // when

        gaugeScanner.analyse(ctx,testReport);
        // then
        assertEquals(1.0,testReport.getScenariosNumber(TestType.FUNCTIONAL_TEST) );


    }
}
