package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestFeatureDto;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by qmxie on 5/18/16.
 */
public class TestFeatureDaoTest extends BaseDaoTest {

    @Before
    public void beforeUnitTest() {
        dbClient.getTestFeatureDao().deleteAll();
    }

    @Test
    public void should_insert_work() {

        dbClient.getTestFeatureDao().insert(nextFeature());

        dbClient.getTestFeatureDao().insert(nextFeature());

        List<TestFeatureDto> ret = dbClient.getTestFeatureDao().selectAll();
        assertEquals(2, ret.size());
    }

    private TestFeatureDto nextFeature() {
        return enhancedRandom.nextObject(TestFeatureDto.class);
    }

    @Test
    public void should_get_work() {

        TestFeatureDto dto = nextFeature();

        dto = dbClient.getTestFeatureDao().insert(dto);

        TestFeatureDto retnull = dbClient.getTestFeatureDao().get(-1);
        assertNull(retnull);

        TestFeatureDto ret = dbClient.getTestFeatureDao().get(dto.getId());
        assertNotNull(ret);
    }

}
