package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

/**
 * Created by qmxie on 5/12/16.
 */
public class SubMyDataDto extends BaseDto{
    private String name;
    private int height;

    public SubMyDataDto() {
    }

    public SubMyDataDto(int height, String name) {
        this.height = height;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
