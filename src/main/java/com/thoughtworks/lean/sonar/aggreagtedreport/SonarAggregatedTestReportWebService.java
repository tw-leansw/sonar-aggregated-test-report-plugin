package com.thoughtworks.lean.sonar.aggreagtedreport;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseJsonWriter;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import org.flywaydb.core.Flyway;
import org.sonar.api.utils.log.*;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;

import java.util.Arrays;
import java.util.List;

public class SonarAggregatedTestReportWebService implements org.sonar.api.server.ws.WebService {

    private TestReportService reportService;

    private Logger LOGGER = Loggers.get(getClass());

    public SonarAggregatedTestReportWebService(Settings settings) {
        reportService = new TestReportService(settings);
    }

    @Override
    public void define(final Context context) {

        LOGGER.info("Deliflow Aggregated Test Report Web Service Init.");
        this.initDB();
        LOGGER.info("Deliflow Aggregated Test Report Web Service Database Migrated.");

        NewController controller = context.createController("api/lean");
        controller.createAction("hello")
                .setHandler(new RequestHandler() {
                    public void handle(Request request, Response response) throws Exception {
                        BaseJsonWriter json = new BaseJsonWriter(response.newJsonWriter());
                        json.writeCollection(Arrays.asList("hello world!"));
                        json.close();
                    }
                });
        controller.createAction("testreport/latest").setHandler(new RequestHandler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                BaseJsonWriter jsonWriter = new BaseJsonWriter(response.newJsonWriter());
                TestReportDto report = reportService.getReport(request.param("project"));
                jsonWriter.writeObject(report);
                jsonWriter.close();
            }
        }).createParam("project").setRequired(true);
        WebService.NewAction getReportAction = controller.createAction("testreport/").setHandler(new RequestHandler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                BaseJsonWriter jsonWriter = new BaseJsonWriter(response.newJsonWriter());
                TestReportDto report = reportService.getReportByBuild(request.param("project"), request.param("build"));
                jsonWriter.writeObject(report);
                jsonWriter.close();
            }
        });
        getReportAction.createParam("project").setRequired(true);
        getReportAction.createParam("build").setRequired(true);

        controller.createAction("testreports").setHandler(new RequestHandler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                BaseJsonWriter jsonWriter = new BaseJsonWriter(response.newJsonWriter());
                List<TestReportDto> reports = reportService.getReports(request.paramAsStrings("projects"));
                jsonWriter.writeCollection(reports);
                jsonWriter.close();
            }
        }).createParam("projects").setRequired(true);
        controller.done();
    }

    private void initDB() {
        Flyway flyway = new Flyway();
        flyway.setClassLoader(this.getClass().getClassLoader());
        // set ClassLoader so flyway can find db/migration
        LOGGER.info(" Set ClassLoader to : " + this.getClass().getClassLoader().toString());
        flyway.setDataSource(reportService.getDataSource());
        flyway.setBaselineOnMigrate(true);
        int ret = flyway.migrate();
        LOGGER.warn(" Init Lean Database finished. Result code: " + ret);
    }
}
