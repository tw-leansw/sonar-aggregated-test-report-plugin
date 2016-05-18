package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.sonar.api.utils.System2;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestStepDao extends AbstractDao {
    public TestStepDao(Mybatis mybatis, System2 system2) {
        super(mybatis, system2);
    }

    public void insert(TestStepDto dto) {
        DbSession session = getDbSession();
        try {
            this.insert(session, dto);
            session.commit();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public void insert(DbSession session, TestStepDto dto) {
        session.getMapper(TestStepMapper.class).insert(dto);
    }

    public List<TestStepDto> selectAll(DbSession session) {
        return session.getMapper(TestStepMapper.class).selectAll();
    }

    public List<TestStepDto> selectAll() {

        DbSession session = getDbSession();
        try {
            return session.getMapper(TestStepMapper.class).selectAll();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public void delete(DbSession session, long id) {
        session.getMapper(TestStepMapper.class).delete(id);
    }

    public void delete(long id) {
        DbSession session = getDbSession();
        try {
            this.delete(session, id);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }


    public void deleteAll() {
        DbSession session = getDbSession();
        session.getMapper(TestStepMapper.class).deleteAll();
    }
}
