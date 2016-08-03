package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.base.Objects;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import org.hamcrest.Matchers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.collection.LambdaCollections.with;

public class TestReportDto extends BaseDto {
    private int id;
    private String projectId;
    private String buildLabel;
    private int duration;

    private Date createTime;
    private Date executionTime;
    private List<TestFeatureDto> testFeatures = new LinkedList<>();

    public TestReportDto() {
        this.createTime = new Date();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TestReportDto setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public BaseDto setParentId(int id) {
        return this;
    }

    @Override
    public List getChildren() {
        return getTestFeatures();
    }

    @Override
    public int getParentId() {
        return -1;
    }

    @Override
    public void calculatingPropsFromChildren() {
        if (this.getTestFeatures().size() > 0) {
            this.setDuration(
                    with(getTestFeatures())
                            .sum(on(TestFeatureDto.class).getDuration()));
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public TestReportDto setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getBuildLabel() {
        return buildLabel;
    }

    public TestReportDto setBuildLabel(String buildLabel) {
        this.buildLabel = buildLabel;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TestReportDto setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public TestReportDto setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public List<TestFeatureDto> getTestFeatures() {
        return testFeatures;
    }

    public TestReportDto addTestFeatures(List<TestFeatureDto> testFeatures) {
        this.testFeatures.addAll(testFeatures);
        this.calculatingPropsFromChildren();
        return this;
    }

    public TestReportDto setTestFeatures(List<TestFeatureDto> testFeatures) {
        this.testFeatures = testFeatures;
        this.calculatingPropsFromChildren();
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public TestReportDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getScenariosNumber(TestType type) {
        return sum(
                with(this.testFeatures).clone()
                        .retain(Matchers.hasProperty("testType", Matchers.equalTo(type)))
                        .extract(on(TestFeatureDto.class).getScenariosNumber())).intValue();
    }

    public List<TestFeatureDto> getFeaturesByTestType(TestType type) {
        return with(this.getTestFeatures())
                .clone()
                .retain(Matchers.hasProperty("testType", Matchers.equalTo(type)));
    }

    public List<TestStepDto> getStepsByResultType(ResultType type) {
        return flatten(with(this.testFeatures)
                .extract(on(TestFeatureDto.class).getStepsByResultType(type)));
    }

    public List<TestScenarioDto> getScenariosByResultType(ResultType type) {
        return flatten(with(this.testFeatures)
                .extract(on(TestFeatureDto.class).getScenariosByResultType(type)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestReportDto that = (TestReportDto) o;
        return id == that.id &&
                duration == that.duration &&
                Objects.equal(projectId, that.projectId) &&
                Objects.equal(buildLabel, that.buildLabel) &&
                Objects.equal(createTime, that.createTime) &&
                Objects.equal(executionTime, that.executionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, projectId, buildLabel, duration, createTime, executionTime);
    }
}
