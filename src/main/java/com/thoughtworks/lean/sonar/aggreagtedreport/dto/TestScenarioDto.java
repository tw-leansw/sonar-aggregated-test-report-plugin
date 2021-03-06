package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.base.Objects;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import org.hamcrest.Matchers;

import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestScenarioDto extends BaseDto {
    private int id;
    private int featureId;

    private String name;
    private ResultType resultType;
    private int duration;
    private List<TestStepDto> testSteps = Lists.newArrayList();

    public TestScenarioDto() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TestScenarioDto setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public BaseDto setParentId(int id) {
        return setFeatureId(id);
    }

    @Override
    public List getChildren() {
        return getTestSteps();
    }

    @Override
    public int getParentId() {
        return this.featureId;
    }

    @Override
    public void calculatingPropsFromChildren() {
        if (this.getTestSteps().size() > 0) {
            this.setDuration(
                    sum(with(getTestSteps())
                            .extract(on(TestStepDto.class)
                                    .getDuration())).intValue());

            Multiset<ResultType> multiset = ConcurrentHashMultiset.create(
                    with(this.getTestSteps())
                            .extract(on(TestStepDto.class).getResultType()));

            int stepPassed = multiset.count(PASSED);
            int stepFailed = multiset.count(FAILED);
            if (stepFailed + stepPassed == 0) {
                this.setResultType(SKIPPED);
            } else {
                this.setResultType(stepPassed == 0 ? FAILED : PASSED);
            }
        }
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

    public List<TestStepDto> getTestSteps() {
        return testSteps;
    }

    public TestScenarioDto setTestSteps(List<TestStepDto> testSteps) {
        this.testSteps = testSteps;
        this.calculatingPropsFromChildren();
        return this;
    }

    public List<TestStepDto> getStepsByResultType(ResultType type) {
        return with(this.getTestSteps()).clone()
                .retain(Matchers.hasProperty("resultType", Matchers.equalTo(type)));
    }

    public int getFeatureId() {
        return featureId;
    }

    public TestScenarioDto setFeatureId(int featureId) {
        this.featureId = featureId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestScenarioDto that = (TestScenarioDto) o;
        return id == that.id &&
                featureId == that.featureId &&
                duration == that.duration &&
                Objects.equal(name, that.name) &&
                resultType == that.resultType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, featureId, name, resultType, duration);
    }
}
