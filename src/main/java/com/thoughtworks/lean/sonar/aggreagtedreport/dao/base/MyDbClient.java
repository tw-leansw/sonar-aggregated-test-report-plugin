//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;


import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestFeatureDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestStepDao;
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
        daoMap.put(MyDataDao.class, new MyDataDao());
        daoMap.put(TestStepDao.class, new TestStepDao());
        daoMap.put(TestFeatureDao.class, new TestFeatureDao());

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

    protected <K extends Dao> K getDao(Map<Class, Dao> map, Class<K> clazz) {
        return (K) map.get(clazz);
    }

    protected <K extends AbstractDao> K getDao(Class<K> clazz) {
        return (K) this.daoMap.get(clazz);
    }

    public Mybatis getMyBatis() {
        return this.myBatis;
    }

    public TestStepDao getTestStepDao() {
        return this.getDao(TestStepDao.class);
    }


    public MyDataDao getMyDataDao() {
        return this.getDao(MyDataDao.class);
    }

    public TestFeatureDao getTestFeatureDao() {
        return this.getDao(TestFeatureDao.class);
    }


}
