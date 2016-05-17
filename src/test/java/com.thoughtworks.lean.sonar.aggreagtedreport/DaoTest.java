package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.MyDataDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.db.DbSession;
import org.sonar.db.DefaultDatabase;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/13/16.
 */
public class DaoTest {
    private static MyDbClient dbClient;
    private DbSession dbSession;

    @BeforeClass
    public static void setUp() {
        Map<String, String> props = Maps.newHashMap();

        props.put("sonar.jdbc.url", "jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        Settings settings = new Settings();
        settings.addProperties(props);
        DefaultDatabase defaultDatabase = new DefaultDatabase(settings);
        defaultDatabase.start();

        Mybatis mybatis = new Mybatis(defaultDatabase);
        dbClient = new MyDbClient(mybatis);
        mybatis.start();
        Flyway flyway = new Flyway();
        flyway.setDataSource(defaultDatabase.getDataSource());
        flyway.migrate();

    }

    @Before
    public void setUpSession() {
        // delete all data before each test
        dbSession = dbClient.openSession(true);
        dbSession.getMapper(MyDataMapper.class).deleteAll();
        dbSession.commit();
        System.out.println(" open db client: " + dbSession.toString());

    }

    @After
    public void tearDown() {
        // delete all data after each test
        dbSession.getMapper(MyDataMapper.class).deleteAll();
        dbSession.commit();
        dbSession.close();
        System.out.println(" close db session: " + dbSession.toString());

    }

    private int getRecordSize() {
        return dbSession.getMapper(MyDataMapper.class).selectAll().size();
    }

    @Test
    public void should_insert_work() {
        MyDataDto dto = new MyDataDto("foo",123);
        dbSession.getMapper(MyDataMapper.class).insert(dto);
        assertEquals(1, getRecordSize());
        dbSession.getMapper(MyDataMapper.class).insert(new MyDataDto("bar",233));
        assertEquals(2, getRecordSize());
        dbSession.commit();
    }


    @Test
    public void should_delete_all_mydata() {
        List<MyDataDto> list = dbSession.getMapper(MyDataMapper.class).selectAll();
        assertEquals(0, list.size());
    }

}
