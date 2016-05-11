package com.thoughtworks.lean.sonar.aggreagtedreport;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.text.JsonWriter;

public class SonarAggregatedTestReportWebService implements WebService {
    @Override
    public void define(Context context) {
        NewController controller = context.createController("api/leansw");
        controller.createAction("hello")
                .setHandler(new RequestHandler() {
                    public void handle(Request request, Response response) throws Exception {
                        JsonWriter json = response.newJsonWriter().beginArray();
                        json.value("hello world!");
                        json.endArray().close();
                    }
                });
        controller.done();
    }
}
