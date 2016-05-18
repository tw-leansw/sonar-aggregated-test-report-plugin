package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;

import java.util.List;

/**
 * Created by qmxie on 5/13/16.
 */
public interface TestStepMapper extends Mapper {

    void insert(TestStepDto dto);

    void delete(long id);

    void deleteAll();

    List<TestStepDto> selectAll();
}
