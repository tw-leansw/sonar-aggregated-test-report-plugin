package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestStepDto extends BaseDto {

    private int id;
    private String name;
    private ResultType resultType;
    private int duration;

    public TestStepDto() {
    }

    public int getId() {
        return id;
    }

    public TestStepDto setId(int id) {
        this.id = id;
        return this;
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


    public int getDuration() {
        return duration;
    }

    public TestStepDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public TestStepDto setDuration(Object duration) {
        this.setDuration(Integer.parseInt(duration.toString()));
        return this;
    }
}
