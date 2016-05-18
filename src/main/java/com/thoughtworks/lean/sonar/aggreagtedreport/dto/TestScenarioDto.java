package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.collect.Lists;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType;
import org.hamcrest.Matchers;

import java.util.Collections;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestScenarioDto extends BaseDto {
    private int id;
    private int featureId;

    private String name;
    private ResultType resultType;
    private int duration;
    private List<TestStepDto> testStepDtoList;

    public TestScenarioDto() {
    }

    public int getId() {
        return id;
    }

    public TestScenarioDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestScenarioDto setName(String name) {
        this.name = name;
        return this;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public TestScenarioDto setResultType(ResultType resultType) {
        this.resultType = resultType;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public TestScenarioDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public List<TestStepDto> getTestStepDtoList() {
        if (this.testStepDtoList == null) {
            return Collections.emptyList();
        }
        return testStepDtoList;
    }

    public TestScenarioDto setTestStepDtoList(List<TestStepDto> testStepDtoList) {
        this.testStepDtoList = testStepDtoList;
        calculateDuration();
        return this;
    }

    private void calculateDuration() {
        setDuration(
                sum(with(getTestStepDtoList()).extract(on(TestStepDto.class).getDuration())).intValue());
    }

    public List<TestStepDto> getStepsByResultType(ResultType type) {
        return with(this.getTestStepDtoList()).clone()
                .retain(Matchers.hasProperty("resultType", Matchers.equalTo(type)));
    }

    public TestScenarioDto addStep(TestStepDto step) {
        if (this.testStepDtoList == null) {
            this.testStepDtoList = Lists.newArrayList();
        }
        this.testStepDtoList.add(step);
        return this;
    }

    public int getFeatureId() {
        return featureId;
    }

    public TestScenarioDto setFeatureId(int featureId) {
        this.featureId = featureId;
        return this;
    }

}
