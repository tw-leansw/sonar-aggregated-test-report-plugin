package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.google.common.collect.Maps;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.TestFeatureDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by qmxie on 5/18/16.
 */
public class BatchDaoTest extends BaseDaoTest {
    static Map<Class, AbstractDao> daoMap;

    @BeforeClass
    public static void setUpMap() {
        daoMap = Maps.newIdentityHashMap();
        daoMap.put(TestFeatureDto.class, dbClient.getTestFeatureDao());
        daoMap.put(TestStepDto.class, dbClient.getTestStepDao());
    }

    @Before
    public void beforeUnitTest() {
        for (AbstractDao dao : daoMap.values()) {
            dao.deleteAll();
        }
    }

    @Test
    public void should_insert_work() {

        for (Map.Entry<Class, AbstractDao> daoEntry : this.daoMap.entrySet()) {
            daoEntry.getValue().insert((BaseDto) enhancedRandom.nextObject(daoEntry.getKey()));
            daoEntry.getValue().insert((BaseDto) enhancedRandom.nextObject(daoEntry.getKey()));

            List<BaseDto> ret = daoEntry.getValue().selectAll();
            assertEquals(2, ret.size());
        }
    }

    @Test
    public void should_get_work() {

        for (Map.Entry<Class, AbstractDao> daoEntry : this.daoMap.entrySet()) {
            BaseDto dto = daoEntry.getValue().insert((BaseDto) enhancedRandom.nextObject(daoEntry.getKey()));

            BaseDto retnull = daoEntry.getValue().get(-1);
            assertNull(retnull);

            BaseDto ret = daoEntry.getValue().get(dto.getId());
            assertNotNull(ret);
        }
    }
}
