package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.AbstractDao;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseDto;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by qmxie on 5/18/16.
 */
public class BatchDaoTest extends BaseTest {
    static Map<Class, AbstractDao> daoMap;

    @BeforeClass
    public static void setUpDaoMap() {
        daoMap = dbClient.getDaoMap();
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
            AbstractDao dao = daoEntry.getValue();
            Class<BaseDto> dtoClass = daoEntry.getKey();

            dao.insert(enhancedRandom.nextObject(dtoClass));
            dao.insert(enhancedRandom.nextObject(dtoClass));

            List<BaseDto> ret = dao.selectAll();
            assertEquals(2, ret.size());
        }
    }

    @Test
    public void should_get_work() {

        for (Map.Entry<Class, AbstractDao> daoEntry : this.daoMap.entrySet()) {
            AbstractDao dao = daoEntry.getValue();
            Class<BaseDto> dtoClass = daoEntry.getKey();

            BaseDto dto = dao.insert(enhancedRandom.nextObject(dtoClass));

            BaseDto retnull = dao.get(-1);
            assertNull(retnull);
            BaseDto ret = dao.get(dto.getId());
            assertEquals(dto, ret);
        }
    }

    @Test
    public void should_delete_work() {
        for (Map.Entry<Class, AbstractDao> daoEntry : this.daoMap.entrySet()) {
            AbstractDao dao = daoEntry.getValue();
            Class<BaseDto> dtoClass = daoEntry.getKey();

            assertEquals(0, dao.selectAll().size());
            BaseDto ret = dao.insert(enhancedRandom.nextObject(dtoClass));
            BaseDto ret2 = dao.insert(enhancedRandom.nextObject(dtoClass));
            assertEquals(2, dao.selectAll().size());
            dao.delete(ret.getId());
            assertEquals(1, dao.selectAll().size());
        }
    }

    @Test
    public void should_update_work() {
        for (Map.Entry<Class, AbstractDao> daoEntry : this.daoMap.entrySet()) {
            AbstractDao dao = daoEntry.getValue();
            Class<BaseDto> dtoClass = daoEntry.getKey();

            BaseDto oriDto = dao.insert(enhancedRandom.nextObject(dtoClass));
            BaseDto newDto = enhancedRandom.nextObject(dtoClass);
            newDto.setId(oriDto.getId());
            dao.update(newDto);

            assertEquals(newDto, dao.get(oriDto.getId()));
        }
    }


}
