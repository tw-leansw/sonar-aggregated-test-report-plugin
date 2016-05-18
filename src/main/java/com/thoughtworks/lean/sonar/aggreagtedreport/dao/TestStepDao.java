package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestStepDao extends AbstractDao<TestStepDto> {
    public TestStepDao() {
        super(TestStepMapper.class);
    }
}
