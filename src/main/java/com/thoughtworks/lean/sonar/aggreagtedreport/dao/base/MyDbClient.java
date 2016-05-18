//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestStepDao;
import org.sonar.api.utils.System2;
import org.sonar.db.Dao;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.IdentityHashMap;
import java.util.Map;

public class MyDbClient {

    private final Mybatis myBatis;

    private final MyDataDao myDataDao;

    private final TestStepDao testStepDao;


    public MyDbClient(Mybatis myBatis) {
        System2 system2=System2.INSTANCE;
        this.myBatis = myBatis;
        this.myDataDao = new MyDataDao(myBatis,system2);
        this.testStepDao = new TestStepDao(myBatis,system2);
    }

    protected void doOnLoad(Map<Class, Dao> daoByClass) {
    }

    public DbSession openSession(boolean batch) {
        return this.myBatis.openSession(batch);
    }

    public void closeSession(DbSession session) {
        MyBatis.closeQuietly(session);
    }


    public MyDataDao getMyDataDao() {
        return myDataDao;
    }

    protected <K extends Dao> K getDao(Map<Class, Dao> map, Class<K> clazz) {
        return (K) map.get(clazz);
    }

    public Mybatis getMyBatis() {
        return this.myBatis;
    }

    public TestStepDao getTestStepDao() {
        return this.testStepDao;
    }
}
