package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public interface Mapper<D extends BaseDto> {
    void deleteAll();
    List<D> selectAll();
    void delete(long id);
    D insert(D dto);
    D get(long id);
    void update(D dto);
}
