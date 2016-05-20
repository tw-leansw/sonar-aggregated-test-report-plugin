package com.thoughtworks.lean.sonar.aggreagtedreport.scanner;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

/**
 * Created by qmxie on 5/20/16.
 */
public class ReportScanner {
    CucumberScanner cucumberScanner;
    GaugeScanner gaugeScanner;
    //JunitScanner junitScanner;

    public ReportScanner(Settings settings, FileSystem projectFileSystem) {
        cucumberScanner = new CucumberScanner(settings, projectFileSystem);
        gaugeScanner = new GaugeScanner(settings, projectFileSystem);
    }



}
