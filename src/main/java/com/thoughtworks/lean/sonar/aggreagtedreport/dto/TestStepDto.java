package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.base.Objects;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;

import java.util.List;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestStepDto extends BaseDto {

    private int id;
    private int scenarioId;
    private String name;
    private ResultType resultType;
    private int duration;

    public TestStepDto() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TestStepDto setId(int id) {
        this.id = id;
        return this;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public TestStepDto setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
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

    @Override
    public BaseDto setParentId(int id) {
        return setScenarioId(id);
    }

    @Override
    public List<BaseDto> getChilds() {
        return null;
    }

    public int getDuration() {
        return duration;
    }

    public TestStepDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestStepDto that = (TestStepDto) o;
        return id == that.id &&
                duration == that.duration &&
                Objects.equal(name, that.name) &&
                resultType == that.resultType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, resultType, duration);
    }
}
