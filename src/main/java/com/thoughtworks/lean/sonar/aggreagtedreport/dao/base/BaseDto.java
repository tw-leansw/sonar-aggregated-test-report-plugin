package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import com.sun.xml.internal.rngom.parse.host.Base;

import java.util.List;

/**
 * Created by qmxie on 5/12/16.
 */
public abstract class BaseDto {

    public BaseDto writeJson(BaseJsonWriter writer) {
        writer.writeObject(this);
        return this;
    }

    public abstract int getId();

    public abstract BaseDto setId(int id);

    public abstract BaseDto setParentId(int id);

    public abstract List getChilds();

    public void setParentIds(){
        for(Object dto:getChilds()){
            ((BaseDto)dto).setParentId(getId());
        }
    }
}
