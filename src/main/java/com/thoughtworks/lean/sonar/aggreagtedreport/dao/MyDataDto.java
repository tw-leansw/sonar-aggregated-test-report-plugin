//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.sonar.api.utils.text.JsonWriter;

public class MyDataDto {
    private String name;
    private int age;


    public MyDataDto() {
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

    public MyDataDto writeJson(JsonWriter json) {
        json.beginObject()
                .prop("name", name)
                .prop("age", age)
                .endObject();
        return  this;

    }


    public String getKey() {
        return this.name;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
