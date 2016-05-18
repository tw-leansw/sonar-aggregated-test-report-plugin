package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import org.sonar.api.utils.System2;
import org.sonar.db.Dao;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public abstract class AbstractDao<T extends Mapper<D>, D extends BaseDto> implements Dao {
    private final Mybatis mybatis;
    private final System2 system2;
    private final Class<T> mapperClass;

    public AbstractDao(Mybatis mybatis, System2 system2, Class<T> mapperClass) {
        this.mapperClass = mapperClass;
        this.mybatis = mybatis;
        this.system2 = system2;
    }



    private Mapper<D> getMapper(DbSession session) {
        return session.getMapper(mapperClass);
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

    //protected abstract   List<D> selectAllImpl(DbSession session);

    public List<D> selectAll() {
        DbSession session = this.getDbSession();
        try {
            return getMapper(session).selectAll();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public void deleteAll(){
        DbSession session = this.getDbSession();
        try {
            getMapper(session).deleteAll();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public D get(long id) {
        DbSession session = this.getDbSession();
        try {
            return getMapper(session).get(id);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public void delete(long id) {
        DbSession session = this.getDbSession();
        try {
            getMapper(session).delete(id);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public void update(D dto){
        DbSession session = this.getDbSession();
        try {
            getMapper(session).update(dto);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public D insert(D dto){
        DbSession session = this.getDbSession();
        try {
            return getMapper(session).insert(dto);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }




}
