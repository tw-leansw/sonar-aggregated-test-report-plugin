package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import ch.lambdaj.Lambda;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestStepDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by qmxie on 5/16/16.
 */
public class TestReport {
    private String pipelineName;
    private String buildTag;
    private Map<TestType, List<TestScenarioDto>> details;

    public TestReport() {
        this.details = Maps.newHashMap();
    }

    public TestReport(String pipelineName, String buildTag) {
        // Todo: how to pass pipeline information to report
        this.pipelineName = pipelineName;
        this.buildTag = buildTag;
        this.details = Maps.newHashMap();
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public TestReport setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
        return this;
    }

    public String getBuildTag() {
        return buildTag;
    }

    public TestReport setBuildTag(String buildTag) {
        this.buildTag = buildTag;
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

    public List<TestScenarioDto> getOrDefault(TestType testType, List<TestScenarioDto> defaultValue) {
        List<TestScenarioDto> retValue = this.details.get(testType);
        return retValue == null ? defaultValue : defaultValue;

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

}
