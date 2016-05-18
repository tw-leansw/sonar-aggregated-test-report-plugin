package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.sonar.api.config.Settings;
import org.sonar.db.DefaultDatabase;

import java.util.Map;

/**
 * Created by qmxie on 5/18/16.
 */
public abstract class BaseDaoTest {
    protected static MyDbClient dbClient;

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
}
