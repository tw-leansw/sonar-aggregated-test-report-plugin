package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestStepDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.thoughtworks.lean.sonar.aggreagtedreport.model.ResultType.PASSED;
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
        TestStepDto dto = new TestStepDto().setName("foo").setResultType(ResultType.FAILED);
        TestStepDto ret = dbClient.getTestStepDao().insert(dto);
        assertEquals(1, getRecordSize());
        assertEquals(dto, ret);
    }


    @Test
    public void should_delete_all_mydata() {
        List<TestStepDto> list = dbClient.getTestStepDao().selectAll();
        assertEquals(0, list.size());
    }

    @Test
    public void should_delete_work(){
        TestStepDto ret = dbClient.getTestStepDao().insert(new TestStepDto().setName("foobar").setResultType(PASSED));
        TestStepDto ret2 = dbClient.getTestStepDao().insert(new TestStepDto().setName("foobar2").setResultType(PASSED));
        System.out.println();
        assertEquals(2,getRecordSize());
        dbClient.getTestStepDao().delete(ret.getId());
        assertEquals(1,getRecordSize());
    }
}
