package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import ch.lambdaj.collection.LambdaList;
import ch.lambdaj.function.convert.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
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
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by qmxie on 5/13/16.
 */
public class CucumberScanner {
    private final static Logger  LOGGER = LoggerFactory.getLogger(CucumberScanner.class);
    private String reportPath;
    private Set<String> componentTestTags;
    private Set<String> functionalTestTags;
    private FileSystem fileSystem;


    public CucumberScanner(Set<String> componentTestTags, Set<String> functionalTestTags) {
        this.componentTestTags = componentTestTags;
        this.functionalTestTags = functionalTestTags;
    }

    public CucumberScanner(Settings settings, FileSystem fs) {
        this.fileSystem = fs;
        this.reportPath = settings.getString("lean.aggregated.test.cucumber.report.path");
        this.componentTestTags = Sets.newHashSet(settings.getStringArray("lean.aggregated.test.cucumber.integration.test.tags"));
        this.functionalTestTags = Sets.newHashSet(settings.getStringArray("lean.aggregated.test.cucumber.functional.test.tags"));

    }

    public void analyse(TestReportDto testReport) {
        try {
            LOGGER.debug("report path: " + this.reportPath);
            analyse(new JXPathMap(new ObjectMapper().readValue(fileSystem.resolvePath(reportPath), Object.class)), testReport);
        } catch (IOException e) {
            LOGGER.warn("cant read cucumber report! reportPath:" + reportPath);
        }
    }

    public void analyse(JXPathMap jxPathMap, TestReportDto testReport) {
        List<Map> features = jxPathMap.get("/");
        LambdaList<JXPathMap> wrappedFeatures = with(features).convert(JXPathMap.toJxPathFunction);
        testReport.addTestFeatures(wrappedFeatures.convert(analyseFeature));
    }

    private Converter<JXPathMap, TestFeatureDto> analyseFeature = new Converter<JXPathMap, TestFeatureDto>() {
        @Override
        public TestFeatureDto convert(JXPathMap feature) {
            List<Map> tags = feature.get("tags");
            Set<String> tagNames = toTagNames(tags);
            TestType testType = getTestType(tagNames);
            String featureName = feature.getString("name");
            String description = feature.getString("description");
            LOGGER.debug(String.format("find cucumber test feature:%s type:%s", featureName, testType.name()));

            List<Map> scenarios = feature.get("elements");
            LambdaList<JXPathMap> wrappedScenarios = with(scenarios)
                    .retain(Matchers.hasEntry("type", "scenario"))
                    .convert(JXPathMap.toJxPathFunction);
            TestFeatureDto testFeatureDto = new TestFeatureDto()
                    .setTestType(testType)
                    .setFrameworkType(TestFrameworkType.CUCUMBER)
                    .setName(featureName)
                    .setDescription(description)
                    .setTestScenarios(wrappedScenarios.convert(analyseScenario));

            return testFeatureDto;
        }
    };


    private TestType getTestType(Set<String> tagNames) {
        TestType testType = TestType.UNIT_TEST;
        if (Sets.intersection(tagNames, functionalTestTags).size() > 0) {
            testType = TestType.FUNCTIONAL_TEST;
        } else if (Sets.intersection(tagNames, componentTestTags).size() > 0) {
            testType = TestType.COMPONENT_TEST;
        }
        return testType;
    }

    Converter<JXPathMap, TestScenarioDto> analyseScenario = new Converter<JXPathMap, TestScenarioDto>() {
        @Override
        public TestScenarioDto convert(JXPathMap scenario) {
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
                                    .setDuration(step.getLong("/result/duration", 0L) / 1000000L)
                                    .setResultType(ResultType.valueOf(step.getString("/result/status").toUpperCase()));
                        }
                    });
            scenarioDto.setTestSteps(stepDtos);
            return scenarioDto;

        }
    };


    public Set<String> toTagNames(List<Map> tags) {
        return with(tags)
                .convert(JXPathMap.toJxPathFunction)
                .extract(on(JXPathMap.class).getString("name"))
                .distinct();
    }
}
