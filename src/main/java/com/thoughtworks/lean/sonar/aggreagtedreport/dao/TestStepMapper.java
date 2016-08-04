package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.CRUDMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by qmxie on 5/13/16.
 */
public interface TestStepMapper extends CRUDMapper<TestStepDto> {

    List<TestStepDto> selectByScenarioIDs(@Param("scenarioIDs") List<Integer> scenarioIDs);
}
