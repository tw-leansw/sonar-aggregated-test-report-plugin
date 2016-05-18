package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestFeatureDao extends AbstractDao<TestFeatureDto> {
    public TestFeatureDao(){
        super(TestFeatureMapper.class);
    }
}
