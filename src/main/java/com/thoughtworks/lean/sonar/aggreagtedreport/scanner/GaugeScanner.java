package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import ch.lambdaj.collection.LambdaList;
import ch.lambdaj.function.convert.Converter;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
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

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.*;

/**
 * Created by qmxie on 5/16/16.
 */
public class GaugeScanner {

    Logger logger = LoggerFactory.getLogger(getClass());
    String reportPath;
    private Set<String> componentTestTags;
    private Set<String> functionalTestTags;
    FileSystem fileSystem;

    public GaugeScanner(Set<String> integrationTestTags, Set<String> functionalTestTags) {
        this.componentTestTags = integrationTestTags;
        this.functionalTestTags = functionalTestTags;
    }

    public GaugeScanner(Settings settings, FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.reportPath = settings.getString("lean.testpyramid.gauge.report.path");
        this.componentTestTags = Sets.newHashSet(settings.getStringArray("lean.testpyramid.gauge.integration.test.tags"));
        this.functionalTestTags = Sets.newHashSet(settings.getStringArray("lean.testpyramid.gauge.functional.test.tags"));
    }

    public void analyse(TestReport testReport) {
        try {
            logger.debug("start gauge test pyramid analyse");
            String reportString = IOUtils.toString(new FileInputStream(fileSystem.resolvePath(reportPath + "html-report/js/result.js")));
            analyse(ScriptUtil.eval(reportString), testReport);
        } catch (IOException e) {
            logger.warn("cant read gauge report!");
        }
    }


    private TestFeatureDto analyseSpec(JXPathMap spec) {
        TestFeatureDto testFeatureDto = new TestFeatureDto();
        List<Map> specItems = spec.selectNodes("/protoSpec/items[@itemType='4']/scenario");

        List<JXPathMap> scenarios = with(specItems).convert(JXPathMap.toJxPathFunction);
        Set<String> tags = spec.getStringSet("/protoSpec/tags");
        double scenarioCount = Double.parseDouble(spec.get("/scenarioCount").toString());
        TestType testType = getTestType(tags);
        String specName = spec.get("/protoSpec/specHeading");
        //testCounter.incrementTestsFor(testType, scenarioCount);
        logger.debug(String.format("find gauge test spec:%s scenarioCount:%.0f type:%s", specName, scenarioCount, testType.name()));
        for (JXPathMap scenario : scenarios) {

            testFeatureDto.getTestScenarios().add(analyseScenario(scenario));
        }
        testFeatureDto.setTestType(testType);
        return testFeatureDto;
    }

    private TestScenarioDto analyseScenario(JXPathMap scenario) {
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
                                .setDuration(step.get("/stepExecutionResult/executionResult/executionTime", 0L));
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


        scenarioDto.setDuration(
                sum(stepDtos.extract(on(TestStepDto.class).getDuration())).longValue());

        Boolean scenarioSkiped = scenario.get("skipped");
        Boolean scenarioFailed = scenario.get("failed");
        if (scenarioSkiped) {
            scenarioDto.setResultType(SKIPPED);
        } else {
            scenarioDto.setResultType(scenarioFailed ? FAILED : PASSED);
        }

        scenarioDto.setTestStepDtoList(stepDtos);


        return scenarioDto;

    }


    public void analyse(JXPathMap jxPathMap, TestReport testReport) {
        List<Map> specResults = jxPathMap.get("/gaugeExecutionResult/suiteResult/specResults");
        LambdaList<JXPathMap> wrapedSpecResults = with(specResults).convert(JXPathMap.toJxPathFunction);
        for (JXPathMap spec : wrapedSpecResults) {
            testReport.addTestFeature(analyseSpec(spec));
        }
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
