package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

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
import org.sonar.api.internal.google.common.collect.Lists;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.lambdaj.Lambda.on;
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
        List<JXPathMap> wrapedFeatures = with(features).convert(JXPathMap.toJxPathFunction);
        for (JXPathMap feature : wrapedFeatures) {
            this.analyseFeature(feature, testReport);
        }
    }

    private void analyseFeature(JXPathMap feature, TestReport testReport) {
        List<Map> tags = feature.get("tags");
        Set<String> tagNames = toTagnames(tags);
        TestType testType = TestType.UNIT_TEST;
        if (Sets.intersection(tagNames, functionalTestTags).size() > 0) {
            testType = TestType.FUNCTIONAL_TEST;
        } else if (Sets.intersection(tagNames, componentTestTags).size() > 0) {
            testType = TestType.COMPONENT_TEST;
        }
        String featureName = feature.getString("name");
        logger.debug(String.format("find cucumber test feature:%s type:%s", featureName, testType.name()));

        List<Map> scenarios = feature.get("elements");
        List<JXPathMap> wrappedScenarios =
                with(scenarios).retain(Matchers.hasEntry("type", "scenario"))
                        .convert(JXPathMap.toJxPathFunction);
        for (JXPathMap senario : wrappedScenarios) {
            this.analyseScenario(senario, testType, testReport);
        }
    }

    private void analyseScenario(JXPathMap senario, TestType testType, TestReport testReport) {
        TestScenarioDto scenarioDto = new TestScenarioDto();
        scenarioDto.setName(senario.getString("name"));

        List<Map> steps = senario.get("steps");
        List<JXPathMap> wrapedSteps = with(steps).convert(JXPathMap.toJxPathFunction);
        List<TestStepDto> stepDtos = Lists.newArrayList();
        boolean allPassed = true;
        long scenrioDuration = 0l;
        for (JXPathMap step : wrapedSteps) {
            TestStepDto stepDto = new TestStepDto()
                    .setName(step.getString("name"))
                    .setDuration(step.get("/result/duration", 0L))
                    .setResultType(ResultType.valueOf(step.getString("/result/status").toUpperCase()));
            scenrioDuration += stepDto.getDuration();
            if (stepDto.getResultType() != PASSED) {
                allPassed = false;
            }
            stepDtos.add(stepDto);
        }
        scenarioDto.setDuration(scenrioDuration);
        scenarioDto.setResultType(allPassed ? PASSED : FAILED);
        scenarioDto.setTestStepDtoList(stepDtos);
        testReport.addScenario(testType, scenarioDto);
    }

    public Set<String> toTagnames(List<Map> tags) {
        List<JXPathMap> wrapedTags = with(tags).convert(JXPathMap.toJxPathFunction);
        return new HashSet<>(with(wrapedTags).extract(on(JXPathMap.class).getString("name")));
    }
}
