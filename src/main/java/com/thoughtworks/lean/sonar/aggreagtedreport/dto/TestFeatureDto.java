package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestFrameWorkType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestFeatureDto {
    private long id;
    private long reportId;
    private TestFrameWorkType frameWorkType;
    private TestType testType;
    private String name;
    private String description;
    private long duration;
    private List<TestScenarioDto> testScenarios=new ArrayList<>();
    private int passedScenarios;
    private int failedScenarios;
    private int skippedScenarios;

    private String buildLabel;
    private Date createTime;
    private Date executionTime;


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

    public TestFrameWorkType getFrameWorkType() {
        return frameWorkType;
    }

    public TestFeatureDto setFrameWorkType(TestFrameWorkType frameWorkType) {
        this.frameWorkType = frameWorkType;
        return this;
    }

    public long getId() {
        return id;
    }

    public TestFeatureDto setId(long id) {
        this.id = id;
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

    public long getDuration() {
        return duration;
    }

    public TestFeatureDto setDuration(long duration) {
        this.duration = duration;
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

    public long getReportId() {
        return reportId;
    }

    public TestFeatureDto setReportId(long reportId) {
        this.reportId = reportId;
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
        return this;
    }
}
