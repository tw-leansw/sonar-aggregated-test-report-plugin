package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.MyDataDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/13/16.
 */
public class MyDataDaoTest extends BaseDaoTest {

    @Before
    public void setUpSession() {
        // delete all data before each test
        dbClient.getMyDataDao().deleteAll();

    }

    @After
    public void tearDown() {
        // delete all data after each test
        dbClient.getMyDataDao().deleteAll();

    }

    private int getRecordSize() {
        return dbClient.getMyDataDao().selectAll().size();
    }

    @Test
    public void should_insert_work() {
        MyDataDto dto = new MyDataDto("foo", 123);
        dbClient.getMyDataDao().insert(dto);
        assertEquals(1, getRecordSize());
        dbClient.getMyDataDao().insert(new MyDataDto("bar", 233));
        assertEquals(2, getRecordSize());
    }


    @Test
    public void should_delete_all_mydata() {
        List<MyDataDto> list = dbClient.getMyDataDao().selectAll();
        assertEquals(0, list.size());
    }

}
