package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import java.util.List;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestScenarioDto {
    private long id;
    private String name;
    private ResultType resultType;
    private double duration;
    List<TestStepDto> testStepDtoList;

    public TestScenarioDto() {
    }

    public long getId() {
        return id;
    }

    public TestScenarioDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<TestStepDto> getTestStepDtoList() {
        return testStepDtoList;
    }

    public void setTestStepDtoList(List<TestStepDto> testStepDtoList) {
        this.testStepDtoList = testStepDtoList;
    }
}
