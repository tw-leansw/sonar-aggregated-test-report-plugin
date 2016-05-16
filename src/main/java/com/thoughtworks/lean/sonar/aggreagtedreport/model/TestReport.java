package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

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

    public void addTag(TestType testType) {
        if (this.details.get(testType) == null){
            this.details.put(testType, Lists.<TestScenarioDto>newArrayList());
        }
    }

    public void addScenario(TestType testType, TestScenarioDto scenarioDto) {
        this.details.get(testType).add(scenarioDto);
    }

    public int getScenariosNumber(TestType type){
        List<TestScenarioDto> scenarioDtos = this.details.getOrDefault(type, Lists.<TestScenarioDto>newArrayList());
        return scenarioDtos.size();
    }
}
