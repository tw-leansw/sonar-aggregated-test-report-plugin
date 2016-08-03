package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestStepDao extends AbstractDao<TestStepDto> {
    public TestStepDao() {
        super(TestStepMapper.class);
    }

    public List<TestStepDto> getByScenarioIds(List<Integer> scenarioIDs) {
        DbSession session = this.getDbSession();
        try {
            return ((TestStepMapper) getMapper(session)).selectByScenarioIDs(scenarioIDs);
        } finally {
            MyBatis.closeQuietly(session);
        }
    }
}
