package com.thoughtworks.lean.sonar.aggreagtedreport.util;

import ch.lambdaj.function.convert.Converter;
import org.apache.commons.jxpath.JXPathContext;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JXPathMap {
    JXPathContext jxPathContext;

    public JXPathMap(Object map) {
        this.jxPathContext = JXPathContext.newContext(map);
    }

    public <T> T get(String key) {
        return (T) jxPathContext.getValue(key);
    }

    public <T> List<T> selectNodes(String key) {
        return (List<T>) jxPathContext.selectNodes(key);
    }

    public <T> T get(String key, T defaultValue) {
        T retValue = (T) jxPathContext.getValue(key);
        return retValue == null ? defaultValue : retValue;
    }

    public long getLong(String key) {
        return Long.parseLong(jxPathContext.getValue(key).toString());
    }

    public long getLong(String key, long defaultValue) {
        Object retValue = jxPathContext.getValue(key);
        return retValue == null ? defaultValue : Long.parseLong(retValue.toString());
    }


    public Set<String> getStringSet(String key) {
        List<String> strings = get(key);
        return strings == null ? new HashSet<String>() : new HashSet<>(strings);
    }

    public String getString(String key) {
        String string = get(key);
        return string;
    }

    public static Converter<Map, JXPathMap> toJxPathFunction = new Converter<Map, JXPathMap>() {
        @Override
        public JXPathMap convert(Map map) {
            return new JXPathMap(map);
        }

    };
}
