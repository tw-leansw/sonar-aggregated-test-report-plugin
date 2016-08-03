package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.CRUDMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;

import java.util.List;

/**
 * Created by qmxie on 5/13/16.
 */
public interface TestScenarioMapper extends CRUDMapper<TestScenarioDto> {

    List<TestScenarioDto> selectByFeatureIDs(List<Integer> featureIDs);
}
