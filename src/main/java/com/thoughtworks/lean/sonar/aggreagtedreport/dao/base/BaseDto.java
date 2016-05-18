package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

/**
 * Created by qmxie on 5/12/16.
 */
public abstract class BaseDto {

    public BaseDto writeJson(BaseJsonWriter writer) {
        writer.writeObject(this);
        return this;
    }

    public abstract int getId();
}
