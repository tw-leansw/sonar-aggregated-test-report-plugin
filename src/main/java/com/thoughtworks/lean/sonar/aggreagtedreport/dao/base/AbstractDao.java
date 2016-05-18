package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import org.sonar.api.utils.System2;
import org.sonar.db.Dao;
import org.sonar.db.DbSession;

/**
 * Created by qmxie on 5/18/16.
 */
public abstract class AbstractDao implements Dao {
    private final Mybatis mybatis;
    private final System2 system2;

    public AbstractDao(Mybatis mybatis, System2 system2) {
        this.mybatis = mybatis;
        this.system2 = system2;
    }

    protected Mybatis mybatis() {
        return this.mybatis;
    }

    protected long now() {
        return this.system2.now();
    }

    protected DbSession getDbSession() {
        return this.mybatis().openSession(false);
    }
    protected DbSession getDbSession(boolean batch) {
        return this.mybatis().openSession(batch);
    }

}
