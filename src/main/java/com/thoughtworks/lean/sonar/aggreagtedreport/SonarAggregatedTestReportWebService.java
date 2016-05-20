package com.thoughtworks.lean.sonar.aggreagtedreport;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseJsonWriter;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.TestReportDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.service.TestReportService;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.db.DbSession;
import org.sonar.db.DefaultDatabase;

import java.util.Arrays;

public class SonarAggregatedTestReportWebService implements org.sonar.api.server.ws.WebService {

    private MyDbClient myDbClient;

    private TestReportService reportService;


    public SonarAggregatedTestReportWebService(Settings settings) {
        reportService = new TestReportService(settings);
    }

    @Override
    public void define(final Context context) {

        NewController controller = context.createController("api/lean");
        controller.createAction("hello")
                .setHandler(new RequestHandler() {
                    public void handle(Request request, Response response) throws Exception {
                        BaseJsonWriter json = new BaseJsonWriter(response.newJsonWriter());
                        json.writeCollection(Arrays.asList("hello world!"));
                        json.close();
                    }
                });
        controller.createAction("report/latest").setHandler(new RequestHandler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                DbSession dbSession = myDbClient.openSession(true);
                BaseJsonWriter jsonWriter = new BaseJsonWriter(response.newJsonWriter());
                TestReportDto report = reportService.getReport(request.param("project"));
                jsonWriter.writeObject(report);
                jsonWriter.close();
                dbSession.close();
            }
        }).createParam("project").setRequired(true);
        controller.done();
    }
}
