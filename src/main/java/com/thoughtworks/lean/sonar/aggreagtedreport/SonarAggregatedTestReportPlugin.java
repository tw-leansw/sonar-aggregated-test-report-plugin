package com.thoughtworks.lean.sonar.aggreagtedreport;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;
import static com.thoughtworks.lean.sonar.aggreagtedreport.Constants.*;


@Properties({
        @Property(key = LEAN_AGGREGATED_TEST_JUNIT_REPORT_PATH, name = "JUnit Report path", defaultValue = "target/surefire-reports/"),
        @Property(key = LEAN_AGGREGATED_TEST_JUNIT_EXCLUDE_TEST_PATTERNS, name = "JUnit Test Exclude (regex)patterns"),
        @Property(key = LEAN_AGGREGATED_TEST_JUNIT_COMPONENT_TEST_PATTERNS, name = "JUnit Component/API/Integration TestCase (regex)patterns", defaultValue = "^IT.*$,^API_.*$,^CT.*$"),
        @Property(key = LEAN_AGGREGATED_TEST_JUNIT_FUNCTIONAL_TEST_PATTERNS, name = "JUnit Functional/UI TestCase (regex)patterns", defaultValue = "^FT.*$,^UI_.*$"),
        //
        @Property(key = LEAN_AGGREGATED_TEST_CUCUMBER_REPORT_PATH, name = "Cucumber Report path (json)", defaultValue = "target/cucumber.json"),
        @Property(key = LEAN_AGGREGATED_TEST_CUCUMBER_COMPONENT_TEST_TAGS, name = "Cucumber Component/API/Integration test tags(@)", defaultValue = "@api_test,@integration_test,@component_test"),
        @Property(key = LEAN_AGGREGATED_TEST_CUCUMBER_FUNCTIONAL_TEST_TAGS, name = "Cucumber Functional/UI test tags(@)", defaultValue = "@functional_test,@ui_test"),
        //
        @Property(key = LEAN_AGGREGATED_TEST_GAUGE_REPORT_PATH, name = "Gauge Report path (read result.js)", defaultValue = "reports/"),
        @Property(key = LEAN_AGGREGATED_TEST_GAUGE_COMPONENT_TEST_TAGS, name = "Gauge Component/API/Integration test tags", defaultValue = "api_test,integration_test,component_test"),
        @Property(key = LEAN_AGGREGATED_TEST_GAUGE_FUNCTIONAL_TEST_TAGS, name = "Gauge Functional/UI test tags", defaultValue = "functional_test,ui_test")

})
public class SonarAggregatedTestReportPlugin extends SonarPlugin {

    public List getExtensions() {
        return Arrays.asList(
                SonarAggregatedTestReportSensor.class,
                SonarAggregatedTestReportWebService.class);
    }
}
