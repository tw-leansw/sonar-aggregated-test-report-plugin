package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TestReportDto {
    private long id;
    private String projectId;
    private String buildLabel;

    private Date createTime;
    private Date executionTime;

    List<TestFeatureDto> testFeatures=new LinkedList<>();


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

    public long getId() {
        return id;
    }

    public TestReportDto setId(long id) {
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
        return this;
    }
}
