//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thoughtworks.lean.sonar.aggreagtedreport.dao;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.MyDataDto;
import org.sonar.api.utils.System2;
import org.sonar.db.DbSession;
import org.sonar.db.MyBatis;

public class MyDataDao extends AbstractDao {

    public MyDataDao(Mybatis mybatis, System2 system) {
        super(mybatis, system, MyDataMapper.class);
    }

    public void insert(MyDataDto dto) {
        DbSession session = this.getDbSession();
        try {
            this.insert(session, dto);
            session.commit();
        } finally {
            MyBatis.closeQuietly(session);
        }
    }

    public void insert(DbSession session, MyDataDto dto) {
        session.getMapper(MyDataMapper.class).insert(dto);
    }


    public void deleteAll() {
        DbSession session = this.getDbSession();
        session.getMapper(MyDataMapper.class).deleteAll();
    }
}
