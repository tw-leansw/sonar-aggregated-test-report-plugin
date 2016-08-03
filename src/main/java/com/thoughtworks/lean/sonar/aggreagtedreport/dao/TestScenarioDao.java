package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestScenarioDao extends AbstractDao<TestScenarioDto> {
    public TestScenarioDao() {
        super(TestScenarioMapper.class);
    }

    public List<TestScenarioDto> getByFeatureIds(List<Integer> featureIDs) {
        DbSession session = this.getDbSession();
        try {
            return ((TestScenarioMapper) getMapper(session)).selectByFeatureIDs(featureIDs);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }
}
