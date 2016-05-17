package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import ch.lambdaj.collection.LambdaList;
import ch.lambdaj.function.convert.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.FAILED;
import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.PASSED;

/**
 * Created by qmxie on 5/13/16.
 */
public class CucumberScanner {
    Logger logger = LoggerFactory.getLogger(getClass());
    String reportPath;
    private Set<String> componentTestTags;
    private Set<String> functionalTestTags;
    FileSystem fileSystem;


    public CucumberScanner(Set<String> componentTestTags, Set<String> functionalTestTags) {
        this.componentTestTags = componentTestTags;
        this.functionalTestTags = functionalTestTags;
    }

    public CucumberScanner(Settings settings, FileSystem fs) {
        this.fileSystem = fs;
        this.reportPath = settings.getString("lean.testpyramid.cucumber.report.path");
        this.componentTestTags = Sets.newHashSet(settings.getStringArray("lean.testpyramid.cucumber.integration.test.tags"));
        this.functionalTestTags = Sets.newHashSet(settings.getStringArray("lean.testpyramid.cucumber.functional.test.tags"));

    }

    public void analyse(TestReport testReport) {
        try {
            analyse(new JXPathMap(new ObjectMapper().readValue(fileSystem.resolvePath(reportPath), Object.class)), testReport);
        } catch (IOException e) {
            logger.warn("cant read cucumber report! reportPath:" + reportPath);
        }
    }

    public void analyse(JXPathMap jxPathMap, TestReport testReport) {
        List<Map> features = jxPathMap.get("/");
        LambdaList<JXPathMap> wrappedFeatures = with(features).convert(JXPathMap.toJxPathFunction);
        for (JXPathMap feature : wrappedFeatures) {
            this.analyseFeature(feature, testReport);
        }
    }

    private void analyseFeature(JXPathMap feature, TestReport testReport) {
        List<Map> tags = feature.get("tags");
        Set<String> tagNames = toTagNames(tags);
        TestType testType = getTestType(tagNames);
        String featureName = feature.getString("name");
        logger.debug(String.format("find cucumber test feature:%s type:%s", featureName, testType.name()));

        List<Map> scenarios = feature.get("elements");
        List<JXPathMap> wrappedScenarios = with(scenarios)
                .retain(Matchers.hasEntry("type", "scenario"))
                .convert(JXPathMap.toJxPathFunction);
        for (JXPathMap scenario : wrappedScenarios) {
            this.analyseScenario(scenario, testType, testReport);
        }
    }

    private TestType getTestType(Set<String> tagNames) {
        TestType testType = TestType.UNIT_TEST;
        if (Sets.intersection(tagNames, functionalTestTags).size() > 0) {
            testType = TestType.FUNCTIONAL_TEST;
        } else if (Sets.intersection(tagNames, componentTestTags).size() > 0) {
            testType = TestType.COMPONENT_TEST;
        }
        return testType;
    }

    private void analyseScenario(JXPathMap scenario, TestType testType, TestReport testReport) {
        TestScenarioDto scenarioDto = new TestScenarioDto();
        scenarioDto.setName(scenario.getString("name"));

        List<Map> steps = scenario.get("steps");

        LambdaList<TestStepDto> stepDtos = with(steps)
                .convert(JXPathMap.toJxPathFunction)
                .convert(new Converter<JXPathMap, TestStepDto>() {
                    @Override
                    public TestStepDto convert(JXPathMap step) {
                        return new TestStepDto()
                                .setName(step.getString("name"))
                                .setDuration(step.get("/result/duration", 0L))
                                .setResultType(ResultType.valueOf(step.getString("/result/status").toUpperCase()));
                    }
                });


        scenarioDto.setDuration(
                sum(stepDtos.extract(on(TestStepDto.class).getDuration())).longValue());

        int stepNotPassed = stepDtos
        .extract(on(TestStepDto.class).getResultType())
                .remove(Matchers.equalTo(PASSED))
                .size();
        scenarioDto.setResultType(stepNotPassed > 0 ? PASSED : FAILED);
        scenarioDto.setTestStepDtoList(stepDtos);
        testReport.addScenario(testType, scenarioDto);

    }

    public Set<String> toTagNames(List<Map> tags) {
        return with(tags)
                .convert(JXPathMap.toJxPathFunction)
                .extract(on(JXPathMap.class).getString("name"))
                .distinct();
    }
}
