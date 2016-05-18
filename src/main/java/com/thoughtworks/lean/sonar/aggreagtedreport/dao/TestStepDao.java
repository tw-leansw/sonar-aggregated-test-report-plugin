package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.sonar.api.utils.System2;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestStepDao extends AbstractDao<TestStepMapper, TestStepDto> {

    public TestStepDao(Mybatis mybatis, System2 system2) {
        super(mybatis, system2, TestStepMapper.class);
    }

}
