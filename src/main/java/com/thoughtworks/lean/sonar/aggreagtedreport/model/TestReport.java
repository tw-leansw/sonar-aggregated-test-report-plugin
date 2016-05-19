package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import ch.lambdaj.Lambda;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by qmxie on 5/16/16.
 */
public class TestReport {

    private TestReportDto testReportDto;
    private String projectId;
    private String buildLabel;

    public TestReport() {
        this.testReportDto = new TestReportDto()
                .setProjectId(this.projectId)
                .setBuildLabel(this.buildLabel)
                .setCreateTime(new Date());
    }

    public TestReport(String projectId, String buildLabel) {


        this.projectId = projectId;
        this.buildLabel = buildLabel;
        this.testReportDto = new TestReportDto()
                .setProjectId(this.projectId)
                .setBuildLabel(this.buildLabel)
                .setCreateTime(new Date());
    }

    public String getProjectId() {
        return projectId;
    }

    public TestReport setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public void addTestFeature(TestFeatureDto testFeatureDto) {
        this.testReportDto.addTestFeature(testFeatureDto);
    }





    public List<TestStepDto> getStepsByResultType(ResultType type) {
        return this.testReportDto.getStepsByResultType(type);
    }

    public TestReportDto getTestReportDto() {
        return testReportDto;
    }

    public TestReport setTestFeatures(List<TestFeatureDto> testFeatures) {
        this.testReportDto.setTestFeatures(testFeatures);
        return this;
    }

    public int getScenariosNumber(TestType type) {
        return this.testReportDto.getScenariosNumber(type);
    }
}
