//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.MyDataDto;
import org.sonar.api.utils.System2;

public class MyDataDao extends AbstractDao<MyDataMapper,MyDataDto> {

    public MyDataDao(Mybatis mybatis, System2 system) {
        super(mybatis, system, MyDataMapper.class);
    }

}
