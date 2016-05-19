package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import com.sun.xml.internal.rngom.parse.host.Base;

import java.io.StringWriter;
import java.util.List;

/**
 * Created by qmxie on 5/12/16.
 */
public abstract class BaseDto {

    public abstract int getId();

    public abstract BaseDto setId(int id);

    public abstract BaseDto setParentId(int id);

    public abstract <T extends BaseDto> List<T> getChilds();

    public void setChildrenzParentId(){
        for(Object dto:getChilds()){
            ((BaseDto)dto).setParentId(getId());
        }
    }

    public BaseDto writeJson(BaseJsonWriter writer) {
        writer.writeObject(this);
        return this;
    }

    public String toJson(){
        StringWriter stringWriter = new StringWriter();
        BaseJsonWriter jsonWriter = BaseJsonWriter.of(stringWriter);
        this.writeJson(jsonWriter);
        return stringWriter.toString();
    }
}
