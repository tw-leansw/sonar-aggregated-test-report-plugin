package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestType;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JXPathMap;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by qmxie on 5/13/16.
 */
public class CucumberScanner {
    Logger logger = LoggerFactory.getLogger(getClass());
    String reportPath;
    private Set<String> integrationTestTags;
    private Set<String> functionalTestTags;
    FileSystem fileSystem;


    public CucumberScanner(Set<String> integrationTestTags, Set<String> functionalTestTags) {
        this.integrationTestTags = integrationTestTags;
        this.functionalTestTags = functionalTestTags;
    }

    public CucumberScanner(Settings settings, FileSystem fs) {
        this.fileSystem = fs;
        this.reportPath = settings.getString("lean.testpyramid.cucumber.report.path");
        this.integrationTestTags = Sets.newHashSet(settings.getStringArray("lean.testpyramid.cucumber.integration.test.tags"));
        this.functionalTestTags = Sets.newHashSet(settings.getStringArray("lean.testpyramid.cucumber.functional.test.tags"));

    }

    public void analyse() {
//        try {
//            analyse(new JXPathMap(new ObjectMapper().readValue(fileSystem.resolvePath(reportPath), Object.class)), testsCounter);
//        } catch (IOException e) {
//            logger.warn("cant read cucumber report! reportPath:" + reportPath);
//        }
    }

    public void analyse(JXPathMap jxPathMap) {
        List<Map> features = jxPathMap.get("/");
        List<JXPathMap> wrapedFeatures = with(features).convert(JXPathMap.toJxPathFunction);
        for (JXPathMap feature : wrapedFeatures) {
            List<Map> tags = feature.get("tags");
            Set<String> tagNames = toTagnames(tags);
            List<Map> scenarios = feature.get("elements");
            List<String> wrappedScenarios = with(scenarios).convert(JXPathMap.toJxPathFunction).extract(on(JXPathMap.class).getString("type")).retain(Matchers.equalTo("scenario"));

            double scenarioCount = wrappedScenarios.size();
            TestType testType = TestType.UNIT_TEST;
            if (Sets.intersection(tagNames, functionalTestTags).size() > 0) {
                testType = TestType.FUNCTIONAL_TEST;
            } else if (Sets.intersection(tagNames, integrationTestTags).size() > 0) {
                //testType = TestType.INTEGRATION_TEST;
            }
            String featureName = feature.getString("name");
            //testsCounter.incrementTestsFor(testType, scenarioCount);
            logger.debug(String.format("find cucumber test feature:%s scenarioCount:%.0f type:%s", featureName, scenarioCount, testType.name()));
        }

    }

    public Set<String> toTagnames(List<Map> tags) {
        List<JXPathMap> wrapedTags = with(tags).convert(JXPathMap.toJxPathFunction);
        return new HashSet<>(with(wrapedTags).extract(on(JXPathMap.class).getString("name")));
    }
}
