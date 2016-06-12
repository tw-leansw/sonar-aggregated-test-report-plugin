package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestReportDao extends AbstractDao<TestReportDto> {
    public TestReportDao() {
        super(TestReportMapper.class);
    }

    public TestReportDto getLatestByProjectId(String projectId) {
        DbSession session = this.getDbSession();
        try {
            return ((TestReportMapper) getMapper(session)).getLatestByProjectId(projectId);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public TestReportDto getTestReport(String projectId, String buildNo) {
        DbSession session = this.getDbSession();
        try {
            return ((TestReportMapper) getMapper(session)).getTestReport(projectId, buildNo);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }
}
