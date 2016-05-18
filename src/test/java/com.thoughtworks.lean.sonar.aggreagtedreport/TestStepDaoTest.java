package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestStepDaoTest extends BaseDaoTest {
    @Before
    public void setUpSession() {
        // delete all data before each test
        dbClient.getTestStepDao().deleteAll();

    }

    @After
    public void tearDown() {
        // delete all data after each test
        dbClient.getTestStepDao().deleteAll();

    }

    private int getRecordSize() {
        return dbClient.getTestStepDao().selectAll().size();
    }

    @Test
    public void should_insert_work() {
        TestStepDto dto = new TestStepDto().setName("foo").setId(2).setResultType(ResultType.FAILED);
        TestStepDto ret = dbClient.getTestStepDao().insert(dto);
        assertEquals(1, getRecordSize());
    }


    @Test
    public void should_delete_all_mydata() {
        List<TestStepDto> list = dbClient.getTestStepDao().selectAll();
        assertEquals(0, list.size());
    }
}
