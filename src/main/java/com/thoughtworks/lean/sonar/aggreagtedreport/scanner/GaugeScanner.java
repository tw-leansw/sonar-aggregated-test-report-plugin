package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import ch.lambdaj.collection.LambdaList;
import ch.lambdaj.function.convert.Converter;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.ScriptUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.Constants.*;
import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;

public class GaugeScanner {

    private final static Logger LOGGER = LoggerFactory.getLogger(GaugeScanner.class);

    private String reportPath;
    private Set<String> componentTestTags;
    private Set<String> functionalTestTags;
    private FileSystem fileSystem;
    private boolean skip = false;

    public GaugeScanner(Set<String> integrationTestTags, Set<String> functionalTestTags) {
        this.componentTestTags = integrationTestTags;
        this.functionalTestTags = functionalTestTags;
    }

    public GaugeScanner(Settings settings, FileSystem fileSystem) {
        this.skip = settings.getBoolean(LEAN_AGGREGATED_TEST_GAUGE_SKIP);
        this.fileSystem = fileSystem;
        this.reportPath = settings.getString(LEAN_AGGREGATED_TEST_GAUGE_REPORT_PATH);
        this.componentTestTags = Sets.newHashSet(settings.getStringArray(LEAN_AGGREGATED_TEST_GAUGE_COMPONENT_TEST_TAGS));
        this.functionalTestTags = Sets.newHashSet(settings.getStringArray(LEAN_AGGREGATED_TEST_GAUGE_FUNCTIONAL_TEST_TAGS));
    }

    public void analyse(TestReportDto testReport) {
        if(skip){
            LOGGER.info("gauge report analyse skipped ");
            return;
        }
        try {
            LOGGER.debug("start gauge test pyramid analyse");
            LOGGER.debug("gauge report path: " + this.reportPath);
            String reportString = IOUtils.toString(new FileInputStream(fileSystem.resolvePath(reportPath + "html-report/js/result.js")));
            analyse(ScriptUtil.eval(reportString), testReport);
        } catch (IOException e) {
            LOGGER.warn("cant read gauge report!");
        }
    }


    private Converter<JXPathMap, TestFeatureDto> analyseSpec = new Converter<JXPathMap, TestFeatureDto>() {
        @Override
        public TestFeatureDto convert(JXPathMap spec) {
            TestFeatureDto testFeatureDto = new TestFeatureDto();


            Set<String> tags = spec.getStringSet("/protoSpec/tags");
            double scenarioCount = Double.parseDouble(spec.get("/scenarioCount").toString());
            TestType testType = getTestType(tags);
            String specName = spec.get("/protoSpec/specHeading");
            //testCounter.incrementTestsFor(testType, scenarioCount);
            LOGGER.debug(String.format("find gauge test spec:%s scenarioCount:%.0f type:%s", specName, scenarioCount, testType.name()));

            List<Map> specItems = spec.selectNodes("/protoSpec/items[@itemType='4']/scenario");
            LambdaList<JXPathMap> scenarios = with(specItems).convert(JXPathMap.toJxPathFunction);
            testFeatureDto.setTestType(testType)
                    .setFrameworkType(TestFrameworkType.GAUGE)
                    .setName(specName)
                    .setTestScenarios(scenarios.convert(analyseScenario));
            return testFeatureDto;
        }
    };

    private Converter<JXPathMap, TestScenarioDto> analyseScenario = new Converter<JXPathMap, TestScenarioDto>() {
        @Override
        public TestScenarioDto convert(JXPathMap scenario) {
            TestScenarioDto scenarioDto = new TestScenarioDto();
            scenarioDto.setName(scenario.getString("/scenarioHeading"));
            List<Map> steps = scenario.selectNodes("/scenarioItems[@itemType='1']/step");

            LambdaList<TestStepDto> stepDtos = with(steps)
                    .convert(JXPathMap.toJxPathFunction)
                    .convert(new Converter<JXPathMap, TestStepDto>() {
                        @Override
                        public TestStepDto convert(JXPathMap step) {
                            TestStepDto testStepDto = new TestStepDto()
                                    .setName(step.getString("/parsedText"))
                                    .setDuration((int) step.getDouble("/stepExecutionResult/executionResult/executionTime", 0.0));
                            Boolean isSkipped = step.get("/stepExecutionResult/skipped");

                            if (isSkipped) {
                                testStepDto.setResultType(SKIPPED);
                            } else {
                                Boolean isFailed = step.get("/stepExecutionResult/executionResult/failed");
                                testStepDto.setResultType(isFailed ? FAILED : PASSED);
                            }
                            return testStepDto;
                        }
                    });
            scenarioDto.setTestSteps(stepDtos);
            return scenarioDto;
        }
    };


    public void analyse(JXPathMap jxPathMap, TestReportDto testReport) {
        List<Map> specResults = jxPathMap.get("/gaugeExecutionResult/suiteResult/specResults");
        LambdaList<JXPathMap> wrappedSpecResults = with(specResults).convert(JXPathMap.toJxPathFunction);
        testReport.addTestFeatures(wrappedSpecResults.convert(analyseSpec));
    }

    private TestType getTestType(Set<String> tags) {
        TestType testType = TestType.UNIT_TEST;
        if (Sets.intersection(tags, functionalTestTags).size() > 0) {
            testType = TestType.FUNCTIONAL_TEST;
        } else if (Sets.intersection(tags, componentTestTags).size() > 0) {
            testType = TestType.COMPONENT_TEST;
        }
        return testType;
    }
}
