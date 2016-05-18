package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import java.util.List;

/**
 * Created by qmxie on 5/18/16.
 */
public interface CRUDMapper<D extends BaseDto> {
    void deleteAll();
    List<D> selectAll();
    void delete(long id);
    void insert(D dto);
    D get(long id);
    D getLastInserted();
    void update(D dto);
}
