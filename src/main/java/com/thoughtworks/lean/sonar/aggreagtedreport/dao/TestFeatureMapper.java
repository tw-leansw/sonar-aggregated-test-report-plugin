package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.CRUDMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;

import java.util.List;

/**
 * Created by qmxie on 5/13/16.
 */
public interface TestFeatureMapper extends CRUDMapper<TestFeatureDto> {

    List<TestFeatureDto> getByReportId(int id);
    List<TestFeatureDto> selectByReportIDs(List<Integer> reportIDs);
}
