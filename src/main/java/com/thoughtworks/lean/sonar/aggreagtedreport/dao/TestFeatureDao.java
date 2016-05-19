package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestFeatureDao extends AbstractDao<TestFeatureDto> {
    public TestFeatureDao() {
        super(TestFeatureMapper.class);
    }

    public List<TestFeatureDto> getByReportId(int id) {
        DbSession session = this.getDbSession();
        try {
            return ((TestFeatureMapper) getMapper(session)).getByReportId(id);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }
}
