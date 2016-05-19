package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.base.Objects;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestFrameworkType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

public class TestFeatureDto extends BaseDto {
    private int id;
    private int reportId;
    private TestFrameworkType frameworkType;
    private TestType testType;
    private String name;
    private String description;
    private int duration;
    private List<TestScenarioDto> testScenarios = new ArrayList<>();
    private int passedScenarios;
    private int failedScenarios;
    private int skippedScenarios;

    private String buildLabel;
    private Date createTime;
    private Date executionTime;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TestFeatureDto setId(int id) {
        this.id = id;
        return this;
    }

    public int getReportId() {
        return reportId;
    }

    public TestFeatureDto setReportId(int reportId) {
        this.reportId = reportId;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public TestFeatureDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TestFeatureDto setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TestFeatureDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public TestFrameworkType getFrameworkType() {
        return frameworkType;
    }

    public TestFeatureDto setFrameworkType(TestFrameworkType frameworkType) {
        this.frameworkType = frameworkType;
        return this;
    }


    public String getName() {
        return name;
    }

    public TestFeatureDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getBuildLabel() {
        return buildLabel;
    }

    public TestFeatureDto setBuildLabel(String buildLabel) {
        this.buildLabel = buildLabel;
        return this;
    }


    public Date getExecutionTime() {
        return executionTime;
    }

    public TestFeatureDto setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public int getFailedScenarios() {
        return failedScenarios;
    }

    public TestFeatureDto setFailedScenarios(int failedScenarios) {
        this.failedScenarios = failedScenarios;
        return this;
    }

    public int getPassedScenarios() {
        return passedScenarios;
    }

    public TestFeatureDto setPassedScenarios(int passedScenarios) {
        this.passedScenarios = passedScenarios;
        return this;
    }


    public int getSkippedScenarios() {
        return skippedScenarios;
    }

    public TestFeatureDto setSkippedScenarios(int skippedScenarios) {
        this.skippedScenarios = skippedScenarios;
        return this;
    }

    public TestType getTestType() {
        return testType;
    }

    public TestFeatureDto setTestType(TestType testType) {
        this.testType = testType;
        return this;
    }

    public List<TestScenarioDto> getTestScenarios() {
        return testScenarios;
    }

    public TestFeatureDto setTestScenarios(List<TestScenarioDto> testScenarios) {
        this.testScenarios = testScenarios;
        calculateDuration();
        return this;
    }

    private void calculateDuration() {
        setDuration(
                with(getTestScenarios())
                        .sum(on(TestScenarioDto.class).getDuration()));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestFeatureDto that = (TestFeatureDto) o;
        return id == that.id &&
                reportId == that.reportId &&
                duration == that.duration &&
                passedScenarios == that.passedScenarios &&
                failedScenarios == that.failedScenarios &&
                skippedScenarios == that.skippedScenarios &&
                frameworkType == that.frameworkType &&
                testType == that.testType &&
                Objects.equal(name, that.name) &&
                Objects.equal(description, that.description) &&
                Objects.equal(buildLabel, that.buildLabel) &&
                Objects.equal(createTime, that.createTime) &&
                Objects.equal(executionTime, that.executionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, reportId, frameworkType, testType, name, description, duration, passedScenarios, failedScenarios, skippedScenarios, buildLabel, createTime, executionTime);
    }
}
