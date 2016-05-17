//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataDao;
import org.sonar.db.Dao;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.IdentityHashMap;
import java.util.Map;

public class MyDbClient {

    private final Mybatis myBatis;

    private final MyDataDao myDataDao;

    public MyDbClient(Mybatis myBatis, Dao... daos) {
        this.myBatis = myBatis;
        IdentityHashMap map = new IdentityHashMap();
        Dao[] arr$ = daos;
        int len$ = daos.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Dao dao = arr$[i$];
            map.put(dao.getClass(), dao);
        }

        this.myDataDao = this.getDao(map, MyDataDao.class);
        this.doOnLoad(map);
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
}
