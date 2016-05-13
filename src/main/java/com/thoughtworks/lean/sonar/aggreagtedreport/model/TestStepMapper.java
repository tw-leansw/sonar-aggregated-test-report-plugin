package com.thoughtworks.lean.sonar.aggreagtedreport.model;

/**
 * Created by qmxie on 5/13/16.
 */
public interface TestStepMapper {

    void insert(TestStepDto dto);

    void delete(long id);
}
