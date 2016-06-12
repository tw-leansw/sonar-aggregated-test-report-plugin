package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.CRUDMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import org.apache.ibatis.annotations.Param;

/**
 * Created by qmxie on 5/18/16.
 */
public interface TestReportMapper extends CRUDMapper<TestReportDto> {
    TestReportDto getLatestByProjectId(String projectID);
    TestReportDto getTestReport(@Param("projectID") String projectID, @Param("buildNo") String buildNo);
}
