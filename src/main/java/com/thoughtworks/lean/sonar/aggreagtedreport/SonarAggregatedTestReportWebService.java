package com.thoughtworks.lean.sonar.aggreagtedreport;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.BaseJsonWriter;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.base.Mybatis;
import com.thoughtworks.lean.sonar.aggreagtedreport.dto.MyDataDto;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.db.DbSession;
import org.sonar.db.DefaultDatabase;

import java.util.Arrays;
import java.util.List;

public class SonarAggregatedTestReportWebService implements org.sonar.api.server.ws.WebService {

    private MyDbClient myDbClient;


    public SonarAggregatedTestReportWebService(Settings settings) {
        DefaultDatabase defaultDatabase = new DefaultDatabase(settings);
        defaultDatabase.start();
        Mybatis mybatis = new Mybatis(defaultDatabase);
        myDbClient = new MyDbClient(mybatis);
        mybatis.start();

    }

    @Override
    public void define(final Context context) {

        NewController controller = context.createController("api/leansw");
        controller.createAction("hello")
                .setHandler(new RequestHandler() {
                    public void handle(Request request, Response response) throws Exception {
                        BaseJsonWriter json = new BaseJsonWriter(response.newJsonWriter());
                        json.writeCollection(Arrays.asList("hello world!"));
                        json.close();
                    }
                });
        controller.createAction("mydata").setHandler(new RequestHandler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                DbSession dbSession = myDbClient.openSession(true);
                BaseJsonWriter jsonWriter = new BaseJsonWriter(response.newJsonWriter());
                List<MyDataDto> list = dbSession.getMapper(MyDataMapper.class).selectAll();

                jsonWriter.writeCollection(list);
                jsonWriter.close();
                dbSession.close();
            }
        });
        controller.done();
    }
}
