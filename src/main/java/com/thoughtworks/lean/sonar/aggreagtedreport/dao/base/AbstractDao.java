package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import org.sonar.db.Dao;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.Collection;
import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public abstract class AbstractDao<D extends BaseDto> implements Dao {
    private Mybatis mybatis;
    private Class mapperClass;

    public AbstractDao setMybatis(Mybatis mybatis) {
        this.mybatis = mybatis;
        return this;
    }

    public <T extends CRUDMapper<D>> AbstractDao(Class<T> mapperClass) {
        this.mapperClass = mapperClass;
    }

    protected CRUDMapper<D> getMapper(DbSession session) {
        return (CRUDMapper<D>) session.getMapper(mapperClass);
    }

    protected Mybatis mybatis() {
        return this.mybatis;
    }

    protected DbSession getDbSession() {
        return this.mybatis().openSession(false);
    }

    protected DbSession getDbSession(boolean batch) {
        return this.mybatis().openSession(batch);
    }

    public List<D> selectAll() {
        DbSession session = this.getDbSession();
        try {
            return selectAll(session);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    private List<D> selectAll(DbSession session) {
        return getMapper(session).selectAll();
    }

    public void deleteAll() {
        DbSession session = this.getDbSession();
        try {
            getMapper(session).deleteAll();
            session.commit();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public D get(long id) {
        DbSession session = this.getDbSession();
        try {
            return get(id, session);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    private D get(long id, DbSession session) {
        return getMapper(session).get(id);
    }


    public void delete(long id) {
        DbSession session = this.getDbSession();
        try {
            delete(id, session);
            session.commit();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    private void delete(long id, DbSession session) {
        getMapper(session).delete(id);
    }

    public void update(D dto) {
        DbSession session = this.getDbSession();
        try {
            getMapper(session).update(dto);
            session.commit();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public D insert(D dto) {
        DbSession session = this.getDbSession();
        try {
            getMapper(session).insert(dto);
            session.commit();
            return dto;
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public Collection<D> insert(Collection<D> collection) {
        DbSession session = this.getDbSession();
        try {
            for (D dto : collection) {
                getMapper(session).insert(dto);
            }
            session.commit();
        } finally {
            Mybatis.closeQuietly(session);
        }
        return collection;
    }

    public List<D> getByParentId(int id) {
        DbSession session = this.getDbSession();
        try {
            return getMapper(session).getByParentId(id);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }


}
