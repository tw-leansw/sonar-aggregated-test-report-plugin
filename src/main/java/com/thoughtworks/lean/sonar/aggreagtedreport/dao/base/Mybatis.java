package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestFeatureMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestReportMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestScenarioMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestStepMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.sonar.api.utils.log.Loggers;
import org.sonar.db.BatchSession;
import org.sonar.db.Database;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatisConfBuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Mybatis {
    private final Database database;
    private SqlSessionFactory sessionFactory;

    public Mybatis(Database database) {
        this.database = database;
    }

    public static void closeQuietly(SqlSession session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception var2) {
                Loggers.get(Mybatis.class).warn("Fail to close db session", var2);
            }
        }

    }

    public Mybatis start() {
        LogFactory.useSlf4jLogging();
        MyBatisConfBuilder confBuilder = new MyBatisConfBuilder(this.database);
        confBuilder.loadAlias("TestStep", TestStepDto.class);
        confBuilder.loadAlias("TestScenario", TestScenarioDto.class);
        confBuilder.loadAlias("TestFeature", TestFeatureDto.class);
        confBuilder.loadAlias("TestReport", TestReportDto.class);
        Class[] mappers = new Class[]{
                TestStepMapper.class,
                TestScenarioMapper.class,
                TestFeatureMapper.class,
                TestReportMapper.class};
        confBuilder.loadMappers(mappers);
        this.sessionFactory = (new SqlSessionFactoryBuilder()).build(confBuilder.build());
        return this;
    }

    public SqlSessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public SqlSession openSession() {
        return this.openSession(false);
    }

    public DbSession openSession(boolean batch) {
        SqlSession session;
        if (batch) {
            session = this.sessionFactory.openSession(ExecutorType.BATCH);
            return new BatchSession(session);
        } else {
            session = this.sessionFactory.openSession(ExecutorType.REUSE);
            return new DbSession(session);
        }
    }

    public PreparedStatement newScrollingSelectStatement(DbSession session, String sql) {
        int fetchSize = this.database.getDialect().getScrollDefaultFetchSize();
        return newScrollingSelectStatement(session, sql, fetchSize);
    }

    public PreparedStatement newScrollingSingleRowSelectStatement(DbSession session, String sql) {
        int fetchSize = this.database.getDialect().getScrollSingleRowFetchSize();
        return newScrollingSelectStatement(session, sql, fetchSize);
    }

    private static PreparedStatement newScrollingSelectStatement(DbSession session, String sql, int fetchSize) {
        try {
            PreparedStatement e = session.getConnection().prepareStatement(sql, 1003, 1007);
            e.setFetchSize(fetchSize);
            return e;
        } catch (SQLException var4) {
            throw new IllegalStateException("Fail to create SQL statement: " + sql, var4);
        }
    }
}
