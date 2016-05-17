package com.thoughtworks.lean.sonar.aggreagtedreport.dto;

import com.thoughtworks.lean.sonar.aggreagtedreport.model.TestFrameWorkType;

public class TestFeatureDto {
    private long hashCode;
    private TestFrameWorkType frameWorkType;
    private String name;
    private String description;
    private long duration;
    private int passed;
    private int failed;
    private int skiped;
    private String buildLabel;

    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public TestFeatureDto setCreateTime(String createTime) {
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

    public long getHashCode() {
        return hashCode;
    }

    public TestFeatureDto setHashCode(long hashCode) {
        this.hashCode = hashCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestFeatureDto setName(String name) {
        this.name = name;
        return this;
    }
}
