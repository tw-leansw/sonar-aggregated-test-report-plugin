package com.thoughtworks.lean.sonar.aggreagtedreport.util;

public class JUnitUtil {
    //TEST-org.thoughtworks.lean.pyramid.IT_1Test.xml
    public static String getTestCaseName(String fileName) {
        String[] fileNames = fileName.split("\\.");
        return fileNames[fileNames.length - 2];
    }
}
