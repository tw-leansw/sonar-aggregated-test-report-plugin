package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestReport;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestType;
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



    public void analyse(JXPathMap jxPathMap, TestReport testReport) {
        List<Map> specResults = jxPathMap.get("/gaugeExecutionResult/suiteResult/specResults");
        List<JXPathMap> wrapedSpecResults = with(specResults).convert(JXPathMap.toJxPathFunction);
        for (JXPathMap spec : wrapedSpecResults) {
            Set<String> tags = spec.getStringSet("protoSpec/tags");
            double scenarioCount = Double.parseDouble(spec.get("scenarioCount").toString());
            TestType testType = TestType.UNIT_TEST;
            if (Sets.intersection(tags, functionalTestTags).size() > 0) {
                testType = TestType.FUNCTIONAL_TEST;
            } else if (Sets.intersection(tags, componentTestTags).size() > 0) {
                testType = TestType.COMPONENT_TEST;
            }
            String specName = spec.get("protoSpec/specHeading");
            //testCounter.incrementTestsFor(testType, scenarioCount);
            logger.debug(String.format("find gauge test spec:%s scenarioCount:%.0f type:%s", specName, scenarioCount, testType.name()));
        }
    }
}