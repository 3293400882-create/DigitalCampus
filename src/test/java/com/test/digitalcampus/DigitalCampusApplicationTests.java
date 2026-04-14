package com.test.digitalcampus;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DigitalCampusApplicationTests {

	@Test
	void contextLoads() {

        Unirest.config().connectTimeout(5000).socketTimeout(10000);

        try {
            // 1. 准备参数
            String nodesJson = "[{\"x\":13410138.73935907,\"y\":3708191.624562681}," +
                    "{\"x\":13409908.048718272,\"y\":3708190.40397728}]";

            String parameterJson = "{"
                    + "\"resultSetting\": {"
                    + "\"returnEdgeIDs\": false,"
                    + "\"returnNodeIDs\": false,"
                    + "\"returnPathGuides\": false,"
                    + "\"returnRoutes\": true,"
                    + "\"returnEdgeFeatures\": true,"
                    + "\"returnEdgeGeometry\": true,"
                    + "\"returnNodeFeatures\": false,"
                    + "\"returnNodeGeometry\": false"
                    + "},"
                    + "\"weightFieldName\": \"长度\","
                    + "\"routeType\": \"RECOMMEND\""
                    + "}";

            // 2. 发起 GET 请求
            HttpResponse<String> response = Unirest.get(
                            "http://localhost:8090/iserver/services/transportationAnalyst-WorkSpace/rest/networkanalyst/DataSource_Network@DataSource/path.json")
                    .queryString("nodes", nodesJson)
                    .queryString("hasLeastEdgeCount", false)
                    .queryString("parameter", parameterJson)
                    .asString();

            // 3. 直接打印响应内容
            System.out.println("响应状态码: " + response.getStatus());
            System.out.println("响应内容:\n" + response.getBody());

        } catch (UnirestException e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();
        }
	}

}
