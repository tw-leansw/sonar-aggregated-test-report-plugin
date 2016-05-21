package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestScenarioDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.exception.LeanPluginException;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.sonar.api.config.Settings;
import org.sonar.db.DefaultDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qmxie on 5/18/16.
 */
public abstract class BaseTest {
    protected static MyDbClient dbClient;
    protected static EnhancedRandom enhancedRandom;

    @BeforeClass
    public static void setUp() {
        Map<String, String> props = Maps.newHashMap();
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

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

    static <T extends BaseDto> List<T> objects(Class<T> clazz, int size, String... strings) {
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ret.add(enhancedRandom.nextObject(clazz, strings));
        }
        return ret;
    }

    static <T extends BaseDto> List<T> setField(List<T> list, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        for (T dto : list) {
            Field fd = dto.getClass().getDeclaredField(field);
            fd.setAccessible(true);
            fd.set(dto, value);
        }
        return list;
    }

    static List<TestStepDto> objectsOfTestStep(int size) {
        try {
            return setField(objects(TestStepDto.class, size),"duration",enhancedRandom.nextInt(500));
        } catch (Exception e) {
            throw new LeanPluginException("Generating random TestStep Error");
        }
    }

    static List<TestScenarioDto> objectsOfTestScenario(int size) {
        return objects(TestScenarioDto.class, size, "testSteps");
    }

    static List<TestFeatureDto> objectsOfTestFeature(int size) {
        return objects(TestFeatureDto.class, size, "testScenarios");
    }

    static List<TestReportDto> objectsOfTestReport(int size) {
        return objects(TestReportDto.class, size, "testFeatures");
    }
}

