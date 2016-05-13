package com.thoughtworks.lean.sonar.aggreagtedreport.model;

import org.sonar.db.Dao;
import org.sonar.db.DbSession;

/**
 * Created by qmxie on 5/13/16.
 */
public class TestStepDao implements Dao{
    public TestStepDao() {
    }

    public void insert(DbSession session, TestStepDto dto){
        ((TestStepMapper)session.getMapper(TestStepMapper.class)).insert(dto);
    }

    public void delete(DbSession session, long id){
        ((TestStepMapper)session.getMapper(TestStepMapper.class)).delete(id);
    }


}
