package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

/**
 * Created by qmxie on 5/12/16.
 */
public abstract class BaseDto {

    public BaseDto writeJson(BaseJsonWriter writer) {
        writer.writeObject(this);
        return this;
    }
}
