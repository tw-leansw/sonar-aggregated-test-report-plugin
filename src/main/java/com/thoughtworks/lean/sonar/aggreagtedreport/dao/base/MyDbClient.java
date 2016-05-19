//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;


import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestFeatureDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestReportDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestScenarioDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestStepDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.sonar.db.Dao;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.Map;

public class MyDbClient {

    private final Mybatis myBatis;

    private Map<Class, AbstractDao> daoMap;


    public MyDbClient(Mybatis myBatis) {
        daoMap = Maps.newIdentityHashMap();
        this.myBatis = myBatis;
        daoMap.put(TestStepDto.class, new TestStepDao());
        daoMap.put(TestFeatureDto.class, new TestFeatureDao());
        daoMap.put(TestScenarioDto.class, new TestScenarioDao());
        daoMap.put(TestReportDto.class, new TestReportDao());

        for (AbstractDao dao : daoMap.values()) {
            dao.setMybatis(this.myBatis);
        }
    }

    protected void doOnLoad(Map<Class, Dao> daoByClass) {
    }

    public DbSession openSession(boolean batch) {
        return this.myBatis.openSession(batch);
    }

    public void closeSession(DbSession session) {
        MyBatis.closeQuietly(session);
    }

    protected <K extends BaseDto,V extends AbstractDao> V getDao(Class<K> clazz) {
        return (V) this.daoMap.get(clazz);
    }

    public Mybatis getMyBatis() {
        return this.myBatis;
    }

    public TestStepDao getTestStepDao() {
        return this.getDao(TestStepDto.class);
    }

    public TestFeatureDao getTestFeatureDao() {
        return this.getDao(TestFeatureDto.class);
    }

    public AbstractDao getTestScenarioDao() {
        return this.getDao(TestScenarioDto.class);
    }

    public Map<Class, AbstractDao> getDaoMap() {
        return daoMap;
    }
}
