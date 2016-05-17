package com.thoughtworks.lean.sonar.aggreagtedreport.dao.base;

import org.sonar.api.utils.text.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

/**
 * Created by qmxie on 5/12/16.
 */
public class BaseJsonWriter implements AutoCloseable {
    JsonWriter jsonWriter;

    public BaseJsonWriter(JsonWriter writer) {
        jsonWriter = writer;
    }

    public static BaseJsonWriter of(Writer writer) {
        return new BaseJsonWriter(JsonWriter.of(writer));
    }

    private BaseJsonWriter writeName(String name) {
        jsonWriter.name(name);
        return this;
    }

    private BaseJsonWriter writeValue(Object fieldValue) {
        if (fieldValue == null) {
            jsonWriter.valueObject(fieldValue);
        } else if (fieldValue instanceof Number) {
            jsonWriter.value((Number) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            jsonWriter.value((Boolean) fieldValue);
        } else if (fieldValue instanceof BaseDto) {
            ((BaseDto) fieldValue).writeJson(this);
        } else if (fieldValue instanceof Date) {
            jsonWriter.valueDateTime((Date) fieldValue);
        } else if (fieldValue instanceof Collection) {
            writeCollection((Collection) fieldValue);
        } else {
            jsonWriter.value(fieldValue.toString());
        }
        return this;
    }

    public BaseJsonWriter writeCollection(Collection fieldValue) {
        jsonWriter.beginArray();
        for (Object o : fieldValue) {
            writeValue(o);
        }
        jsonWriter.endArray();
        return this;
    }

    public BaseJsonWriter writeObject(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        jsonWriter.beginObject();
        for (Field field : fields) {
            writeField(field, o);
        }
        jsonWriter.endObject();
        return this;
    }

    private BaseJsonWriter writeField(Field field, Object o) {
        try {
            field.setAccessible(true);
            writeName(field.getName());
            writeValue(field.get(o));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void close() throws IOException {
        if (jsonWriter != null) {
            jsonWriter.close();
        }
    }
}
