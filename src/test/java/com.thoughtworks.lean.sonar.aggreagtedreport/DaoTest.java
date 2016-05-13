package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.Mybatis;
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
        props.put("sonar.jdbc.url", "jdbc:mysql://sonarqube-server:3306/sonarqube?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true");
        props.put("sonar.jdbc.username", "sonarqube");
        props.put("sonar.jdbc.password", "sonarqube");
        Settings settings = new Settings();
        settings.addProperties(props);
        DefaultDatabase defaultDatabase = new DefaultDatabase(settings);
        defaultDatabase.start();
        Mybatis mybatis = new Mybatis(defaultDatabase);
        dbClient = new MyDbClient(mybatis);
        mybatis.start();
    }

    @Before
    public void setUpSession() {
        // delete all data before each test
        dbSession = dbClient.openSession(true);
        dbSession.getMapper(MyDataMapper.class).deleteAll();
        System.out.println(" open db client: " + dbSession.toString());

    }

    @After
    public void tearDown() {
        // delete all data after each test
        dbSession.getMapper(MyDataMapper.class).deleteAll();
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
    }


    @Test
    public void should_delete_all_mydata() {
        List<MyDataDto> list = dbSession.getMapper(MyDataMapper.class).selectAll();
        assertEquals(0, list.size());
    }

}
