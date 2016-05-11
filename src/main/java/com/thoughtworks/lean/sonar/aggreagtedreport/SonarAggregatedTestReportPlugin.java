package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.ImmutableList;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.List;


@Properties({
        @Property(key = "lean.aggregated.test.junit.report.path", name = "JUnit Report path", defaultValue = "target/surefire-reports/"),
        @Property(key = "lean.aggregated.test.junit.exclude.test.patterns", name = "JUnit Test Exclude (regex)patterns"),
        @Property(key = "lean.aggregated.test.junit.integration.test.patterns", name = "JUnit Integration/API TestCase (regex)patterns", defaultValue = "^IT.*$,^API_.*$"),
        @Property(key = "lean.aggregated.test.junit.functional.test.patterns", name = "JUnit Functional/UI TestCase (regex)patterns", defaultValue = "^FT.*$,^UI_.*$"),
        //
        @Property(key = "lean.aggregated.test.cucumber.report.path", name = "Cucumber Report path (json)", defaultValue = "target/cucumber.json"),
        @Property(key = "lean.aggregated.test.cucumber.integration.test.tags", name = "Cucumber Integration test tags(@)", defaultValue = "@api_test,@integration_test"),
        @Property(key = "lean.aggregated.test.cucumber.functional.test.tags", name = "Cucumber Functional/UI test tags(@)", defaultValue = "@functional_test,@ui_test"),
        //
        @Property(key = "lean.aggregated.test.gauge.report.path", name = "Gauge Report path (read result.js)", defaultValue = "reports/"),
        @Property(key = "lean.aggregated.test.gauge.integration.test.tags", name = "Gauge Integration test tags", defaultValue = "api_test,integration_test"),
        @Property(key = "lean.aggregated.test.gauge.functional.test.tags", name = "Gauge Functional/UI test tags", defaultValue = "functional_test,ui_test")

}
)
public class SonarAggregatedTestReportPlugin extends SonarPlugin {

    public List getExtensions() {
        return ImmutableList.of(
                SonarAggregatedTestReportSensor.class,
                SonarAggregatedTestPyramidMetrics.class,
                SonarAggregatedTestReportWebService.class
        );
    }
}
