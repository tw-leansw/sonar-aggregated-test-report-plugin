//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.MyDataDto;

import java.util.List;

public interface MyDataMapper extends Mapper {

    void insert(MyDataDto var1);

    List<MyDataDto> selectAll();

    void deleteAll();
}
