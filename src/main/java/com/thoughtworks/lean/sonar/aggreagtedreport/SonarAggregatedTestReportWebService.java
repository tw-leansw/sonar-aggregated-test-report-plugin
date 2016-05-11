package com.thoughtworks.lean.sonar.aggreagtedreport;


import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataDto;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDataMapper;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.MyDbClient;
import com.thoughtworks.lean.sonar.aggreagtedreport.dao.Mybatis;
import org.apache.ibatis.session.SqlSessionFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;
import org.sonar.db.DbSession;
import org.sonar.db.DefaultDatabase;

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
                        JsonWriter json = response.newJsonWriter().beginArray();
                        json.value("hello world!");
                        json.endArray().close();
                    }
                });
        controller.createAction("mydata").setHandler(new RequestHandler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                DbSession dbSession = myDbClient.openSession(true);
                JsonWriter jsonWriter = response.newJsonWriter().beginArray();
                List<MyDataDto> list = dbSession.getMapper(MyDataMapper.class).selectAll();
                for (MyDataDto myDataDto : list) {
                    myDataDto.writeJson(jsonWriter);
                }
                jsonWriter.endArray().close();
                dbSession.close();
            }
        });
        controller.done();
    }
}
