package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.CRUDMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public interface TestReportMapper extends CRUDMapper<TestReportDto> {
    TestReportDto getLatestByProjectId(String projectID);
    TestReportDto getTestReport(@Param("projectId") String projectID, @Param("buildNo") String buildNo);
    List<TestReportDto> selectByProjectIDs(@Param("projectIDs") List<String> projectIDs);
}
