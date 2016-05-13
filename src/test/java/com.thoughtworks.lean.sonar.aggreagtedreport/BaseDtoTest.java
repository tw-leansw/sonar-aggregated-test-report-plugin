package com.thoughtworks.lean.sonar.aggreagtedreport;

import com.thoughtworks.lean.sonar.aggreagtedreport.dao.BaseJsonWriter;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.SubMyDataDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by qmxie on 5/12/16.
 */
public class BaseDtoTest {

    BaseJsonWriter jsonWriter;
    StringWriter stringWriter;

    @Before
    public void setUp(){
        stringWriter = new StringWriter();
        jsonWriter = BaseJsonWriter.of(stringWriter);
    }

    @After
    public void tearDown() throws IOException {
        jsonWriter.close();
        stringWriter.close();
    }

    @Test
    public void should_return_json() throws IOException {

        MyDataDto dto = new MyDataDto();
        dto.setAge(10).setName("mmm");
        dto.writeJson(jsonWriter);
        assertEquals("{\"name\":\"mmm\",\"age\":10}",stringWriter.toString());
    }

    @Test
    public void should_return_nested_json(){

        SubMyDataDto subMyDataDto = new SubMyDataDto(234,"xxxxx");
        MyDataDto dto = new MyDataDto();
        dto.setAge(10).setName("mmm");
        dto.setSubMyDataDto(subMyDataDto);

        dto.writeJson(jsonWriter);
        assertEquals("{\"name\":\"mmm\",\"age\":10,\"subMyDataDto\":{\"name\":\"xxxxx\",\"height\":234}}",stringWriter.toString());
    }

    @Test
    public void should_return_list_in_json(){

        SubMyDataDto subMyDataDto = new SubMyDataDto(234,"xxxxx");
        MyDataDto dto = new MyDataDto();
        dto.setAge(10).setName("mmm");
        dto.setSubMyDataDto(subMyDataDto);
        List list = Arrays.asList(new Integer[]{2,2,3,4,5});
        dto.setList(list);

        dto.writeJson(jsonWriter);
        assertEquals("{\"name\":\"mmm\",\"age\":10,\"subMyDataDto\":{\"name\":\"xxxxx\",\"height\":234},\"list\":[2,2,3,4,5]}",stringWriter.toString());

    }

    @Test
    public void should_return_list_object_in_json(){

        SubMyDataDto subMyDataDto = new SubMyDataDto(234,"xxxxx");
        MyDataDto dto = new MyDataDto();
        dto.setAge(10).setName("mmm");
        dto.setSubMyDataDto(subMyDataDto);
        List list = Arrays.asList(new SubMyDataDto[]{
                new SubMyDataDto(1,"a"),
                new SubMyDataDto(2,"b"),
                new SubMyDataDto(3,"c"),
                new SubMyDataDto(4,"d")
        });
        dto.setList(list);

        dto.writeJson(jsonWriter);
        assertEquals("{\"name\":\"mmm\",\"age\":10,\"subMyDataDto\":{\"name\":\"xxxxx\",\"height\":234},\"list\":[{\"name\":\"a\",\"height\":1},{\"name\":\"b\",\"height\":2},{\"name\":\"c\",\"height\":3},{\"name\":\"d\",\"height\":4}]}",stringWriter.toString());

    }


}
