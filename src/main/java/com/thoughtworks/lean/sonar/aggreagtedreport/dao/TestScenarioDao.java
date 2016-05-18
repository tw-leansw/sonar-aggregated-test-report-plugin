package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestScenarioDao extends AbstractDao<TestScenarioDto>{
    public TestScenarioDao() {
        super(TestScenarioMapper.class);
    }
}
