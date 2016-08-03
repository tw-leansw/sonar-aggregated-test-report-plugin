package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.google.common.base.Objects;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;
import static com.thoughtworks.lean.sonar.aggreagtedreport.dto.ResultType.*;

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

    private Date createTime;
    private Date executionTime;

    public TestFeatureDto() {
        this.createTime = new Date();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TestFeatureDto setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public BaseDto setParentId(int id) {
        return setReportId(id);
    }

    @Override
    public List getChildren() {
        return getTestScenarios();
    }

    @Override
    public int getParentId() {
        return this.reportId;
    }

    @Override
    public void calculatingPropsFromChildren() {
        if (this.getTestScenarios().size() > 0) {
            this.setDuration(
                    with(getTestScenarios())
                            .sum(on(TestScenarioDto.class).getDuration()));

            Multiset<ResultType> multiset = ConcurrentHashMultiset.create(
                    with(this.getTestScenarios())
                            .extract(on(TestScenarioDto.class).getResultType()));
            this.setPassedScenarios(multiset.count(PASSED));
            this.setFailedScenarios(multiset.count(FAILED));
            this.setSkippedScenarios(multiset.count(SKIPPED));
        }
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
        this.calculatingPropsFromChildren();
        return this;
    }

    public int getScenariosNumber() {
        return this.testScenarios.size();
    }

    public List<TestStepDto> getStepsByResultType(ResultType type) {
        return flatten(with(this.testScenarios)
                .extract(on(TestScenarioDto.class).getStepsByResultType(type)));
    }

    public List<TestScenarioDto> getScenariosByResultType(ResultType type) {
        return with(this.getTestScenarios())
                .clone()
                .retain(Matchers.hasProperty("resultType", Matchers.equalTo(type)));
    }

    public TestScenarioDto getScenarioByName(String name) {
        return with(this.getTestScenarios())
                .first(Matchers.hasProperty("name", Matchers.equalTo(name)));
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
                Objects.equal(createTime, that.createTime) &&
                Objects.equal(executionTime, that.executionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, reportId, frameworkType, testType, name, description, duration, passedScenarios, failedScenarios, skippedScenarios, createTime, executionTime);
    }
}
