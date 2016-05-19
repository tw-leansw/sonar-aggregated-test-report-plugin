package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.base.Objects;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

public class TestReportDto extends BaseDto {
    private int id;
    private String projectId;
    private String buildLabel;
    private int duration;

    private Date createTime;
    private Date executionTime;

    List<TestFeatureDto> testFeatures = new LinkedList<>();




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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TestReportDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public TestReportDto setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }


    public List<TestFeatureDto> getTestFeatures() {
        return testFeatures;
    }

    public TestReportDto setTestFeatures(List<TestFeatureDto> testFeatures) {
        this.testFeatures = testFeatures;
        calculateDuration();
        return this;
    }

    private void calculateDuration() {
        setDuration(
                with(getTestFeatures())
                        .sum(on(TestFeatureDto.class).getDuration()));
    }

    public int getDuration() {
        return duration;
    }

    public TestReportDto setDuration(int duration) {
        this.duration = duration;
        return this;
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
