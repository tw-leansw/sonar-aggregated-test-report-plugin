package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.BaseDto;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestStepDto extends BaseDto{

    private String name;
    private ResultType resultType;
    private double duration;

    public TestStepDto() {
    }

    public String getName() {
        return name;
    }

    public TestStepDto setName(String name) {
        this.name = name;
        return this;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public TestStepDto setResultType(ResultType resultType) {
        this.resultType = resultType;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public TestStepDto setDuration(double duration) {
        this.duration = duration;
        return this;
    }
}
