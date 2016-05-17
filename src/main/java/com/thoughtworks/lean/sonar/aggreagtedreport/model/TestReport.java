package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import ch.lambdaj.Lambda;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by qmxie on 5/16/16.
 */
public class TestReport {

    private TestReportDto testReportDto;
    private String projectId;
    private String buildLabel;
    private Map<TestType, List<TestScenarioDto>> details;

    public TestReport() {
        this.details = Maps.newHashMap();
        this.testReportDto = new TestReportDto()
                .setProjectId(this.projectId)
                .setBuildLabel(this.buildLabel)
                .setCreateTime(new Date());
    }

    public TestReport(String projectId, String buildLabel) {


        this.projectId = projectId;
        this.details = Maps.newHashMap();
        this.buildLabel = buildLabel;
        this.testReportDto = new TestReportDto()
                .setProjectId(this.projectId)
                .setBuildLabel(this.buildLabel)
                .setCreateTime(new Date());
    }

    public String getProjectId() {
        return projectId;
    }

    public TestReport setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }


    public Map<TestType, List<TestScenarioDto>> getDetails() {
        return details;
    }

    public void addScenario(TestType testType, TestScenarioDto scenarioDto) {
        if (this.details.get(testType) == null) {
            this.details.put(testType, Lists.<TestScenarioDto>newArrayList());
        }
        this.details.get(testType).add(scenarioDto);
    }

    public void addTestFeature(TestFeatureDto testFeatureDto) {
        for (TestScenarioDto testScenarioDto : testFeatureDto.getTestScenarios()) {
            addScenario(testFeatureDto.getTestType(), testScenarioDto);
        }
    }

    public void setTestFeatures(List<TestFeatureDto> testFeatureDtos) {
        for (TestFeatureDto testFeatureDto : testFeatureDtos) {
            addTestFeature(testFeatureDto);
        }
        testReportDto.setTestFeatures(testFeatureDtos);
    }

    private List<TestScenarioDto> getOrDefault(TestType testType, List<TestScenarioDto> defaultValue) {
        List<TestScenarioDto> retValue = this.details.get(testType);
        return retValue == null ? defaultValue : retValue;

    }

    public List<TestScenarioDto> getScenarios(TestType type) {
        return getOrDefault(type, Collections.<TestScenarioDto>emptyList());
    }

    public int getScenariosNumber(TestType type) {
        return this.getScenarios(type).size();
    }

    public List<TestStepDto> getStepsByResultType(ResultType type) {
        List<TestStepDto> res = Lists.newArrayList();
        for (List<TestScenarioDto> scenarioDtos : this.details.values()) {
            res.addAll(Lambda.<TestStepDto>flatten(
                    with(scenarioDtos)
                            .extract(on(TestScenarioDto.class).getStepsByResultType(type))));
        }
        return res;
    }

    public TestReportDto getTestReportDto() {
        return testReportDto;
    }
}
