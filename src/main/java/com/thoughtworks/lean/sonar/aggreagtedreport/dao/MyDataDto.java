//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class MyDataDto extends BaseDto{
    private String name;
    private int age;
    private SubMyDataDto subMyDataDto;
    List<Object> list;

    public MyDataDto() {
    }

    public MyDataDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public List<Object> getList() {
        return list;
    }

    public void  setList(List<Object> list) {
        this.list = list;
    }

    public SubMyDataDto getSubMyDataDto() {
        return subMyDataDto;
    }

    public void setSubMyDataDto(SubMyDataDto subMyDataDto) {
        this.subMyDataDto = subMyDataDto;
    }

    public MyDataDto setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public MyDataDto setAge(int age) {
        this.age = age;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return this.name;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
