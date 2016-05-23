package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import ch.lambdaj.function.convert.Converter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;
import com.thoughtworks.lean.sonar.aggreagtedreport.util.JUnitUtil;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.Constants.*;
import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;


public class JUnitScanner {
    private final static Logger LOGGER = LoggerFactory.getLogger(JUnitScanner.class);
    private String reportPath;
    private Set<String> componentPatterns;
    private Set<String> functionalPatterns;
    private Set<String> excludePatterns;
    private FileSystem fileSystem;
    private boolean skip = false;

    public JUnitScanner(Set<String> componentTestTags, Set<String> functionalTestTags) {
        this.componentPatterns = componentTestTags;
        this.functionalPatterns = functionalTestTags;
    }

    public JUnitScanner(Settings settings, FileSystem fs) {
        this.fileSystem = fs;
        this.reportPath = settings.getString(LEAN_AGGREGATED_TEST_JUNIT_REPORT_PATH);
        this.excludePatterns = Sets.newHashSet(settings.getStringArray(LEAN_AGGREGATED_TEST_JUNIT_EXCLUDE_TEST_PATTERNS));
        this.componentPatterns = Sets.newHashSet(settings.getStringArray(LEAN_AGGREGATED_TEST_JUNIT_COMPONENT_TEST_PATTERNS));
        this.functionalPatterns = Sets.newHashSet(settings.getStringArray(LEAN_AGGREGATED_TEST_GAUGE_FUNCTIONAL_TEST_TAGS));
        this.skip = settings.getBoolean(LEAN_AGGREGATED_TEST_JUNIT_SKIP);
    }

    public void analyse(TestReportDto testreport) {
        if (skip) {
            LOGGER.info("junit report analyse skipped");
            return;
        }
        File dir = fileSystem.resolvePath(reportPath);
        analyse(testreport, dir);
    }

    public void analyse(TestReportDto testReport, File dir) {
        LOGGER.debug("junit report path: " + this.reportPath);
        if (dir.exists() && dir.isDirectory()) {
            List<File> files = new ArrayList<>(FileUtils.listFiles(dir, new String[]{"xml"}, false));
            testReport.addTestFeatures(with(files).convert(analyseFile).retain(Matchers.notNullValue()));
        } else {
            LOGGER.warn("junit report directory is not exsits!");
        }
    }

    private Converter<File, TestFeatureDto> analyseFile = new Converter<File, TestFeatureDto>() {
        @Override
        public TestFeatureDto convert(File file) {
            String testFeature = JUnitUtil.getTestFeatureName(file.getName());
            if (isExcluded(testFeature)) {
                LOGGER.debug("junit test file " + testFeature + " skipped/");
                return null;
            }
            TestFeatureDto testFeatureDto = new TestFeatureDto()
                    .setName(testFeature)
                    .setFrameworkType(TestFrameworkType.JUNIT);

            try {
                testFeatureDto.setTestScenarios(
                        with(Jsoup.parse(file, "UTF-8").select("testcase"))
                                .convert(analyseScenario));
            } catch (IOException e) {
                LOGGER.warn("read junit report file error!");
            }

            if (isFunctionalTest(testFeature)) {
                testFeatureDto.setTestType(TestType.FUNCTIONAL_TEST);
            } else if (isComponentTest(testFeature)) {
                testFeatureDto.setTestType(TestType.COMPONENT_TEST);
            } else {
                testFeatureDto.setTestType(TestType.UNIT_TEST);
            }
            return testFeatureDto;
        }
    };

    private Converter<Element, TestScenarioDto> analyseScenario = new Converter<Element, TestScenarioDto>() {
        @Override
        public TestScenarioDto convert(Element element) {
            TestScenarioDto scenarioDto = new TestScenarioDto()
                    .setName(element.attr("name"))
                    .setDuration(JUnitUtil.transSecondsToNanos(element.attr("time")));
            if (hasError(element) || hasFailure(element)) {
                scenarioDto.setResultType(FAILED);
            } else if (hasSkipped(element)) {
                scenarioDto.setResultType(SKIPPED);
            } else {
                scenarioDto.setResultType(PASSED);
            }
            return scenarioDto;
        }
    };

    public boolean hasError(Element element) {
        return element.select("error").size() > 0;
    }

    public boolean hasFailure(Element element) {
        return element.select("failure").size() > 0;
    }

    public boolean hasSkipped(Element element) {
        return element.select("skipped").size() > 0;
    }

    public boolean checkPatterns(final String str, Set<String> patterns) {
        if (patterns == null) {
            return false;
        }
        List<String> pattenList = Lists.newArrayList(patterns.iterator());
        List<Boolean> booleans = with(pattenList).convert(new Converter<String, Boolean>() {
            @Override
            public Boolean convert(String pattern) {
                Pattern regPattern = Pattern.compile(pattern);
                Matcher matcher = regPattern.matcher(str);
                return matcher.matches();
            }
        });
        for (Boolean bool : booleans) {
            if (bool) {
                return true;
            }
        }
        return false;
    }

    public boolean isComponentTest(String testCase) {
        return checkPatterns(testCase, this.componentPatterns);
    }

    public boolean isFunctionalTest(String testCase) {
        return checkPatterns(testCase, this.functionalPatterns);
    }

    public boolean isExcluded(String testCase) {
        return checkPatterns(testCase, this.excludePatterns);
    }

    public JUnitScanner setReportPath(String reportPath) {
        this.reportPath = reportPath;
        return this;
    }
}
