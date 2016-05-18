package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestReportDao extends AbstractDao<TestReportDto> {
    public TestReportDao() {
        super(TestReportMapper.class);
    }
}
