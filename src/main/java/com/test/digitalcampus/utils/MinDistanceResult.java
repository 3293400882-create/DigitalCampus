package com.test.digitalcampus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.digitalcampus.pojo.PathRequest;
import com.test.digitalcampus.pojo.PathResult;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MinDistanceResult {

     //Unirest.config().connectTimeout(5000).socketTimeout(10000);
    private static final double ORIGIN_SHIFT = Math.PI * 6378137.0;
    private static final ObjectMapper mapper = new ObjectMapper();

    private String parameterJson = "{"
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

    /**
     * 获取最短路径并加工为 PathResult
     */

    public PathResult getPathResult(List<PathRequest> nodes) {
        String rawJson = getPathJson(nodes);
        if ("缺少参数".equals(rawJson)) {
            return null;
        }
        return extract(rawJson);
    }

    public String getPathJson(List<PathRequest> nodes) {
        try {
            // 关键修改：把 List 转成 JSON 字符串
            String nodesJson = mapper.writeValueAsString(nodes);
            log.debug("请求参数 nodes: {}", nodesJson);

            HttpResponse<String> response = Unirest.get(
                            "http://localhost:8090/iserver/services/transportationAnalyst-WorkSpace/rest/networkanalyst/DataSource_Network@DataSource/path.json")
                    .queryString("nodes", nodesJson)  // 传 JSON 字符串
                    .queryString("hasLeastEdgeCount", false)
                    .queryString("parameter", parameterJson)
                    .asString();

            log.info("响应状态码: {}", response.getStatus());

            if (response.getStatus() != 200) {
                log.error("请求失败: {}", response.getBody());
                return "请求失败";
            }

            return response.getBody();
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
            return "缺少参数";
        } catch (Exception e) {
            log.error("请求异常", e);
            return "缺少参数";
        }
    }
    /**
     * 从 SuperMap JSON 提取并转换坐标
     */
    public PathResult extract(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            return extract(root);
        } catch (IOException e) {
            log.error("JSON解析失败", e);
            return null;
        }
    }

    /**
     * 从 JsonNode 提取路径
     */
    public PathResult extract(JsonNode root) {
        PathResult result = new PathResult();
        List<double[]> mercatorPoints = new ArrayList<>();

        // 提取: pathList[0].route.line.points
        JsonNode pathList = root.path("pathList");
        if (!pathList.isArray() || pathList.size() == 0) {
            log.warn("未找到 pathList");
            return result;
        }
        JsonNode firstPath = pathList.get(0);
        // 提取总距离
        result.setTotalDistance(firstPath.path("weight").asDouble(0));
        // 提取坐标点
        JsonNode pointsNode = firstPath
                .path("route")
                .path("line")
                .path("points");

        if (pointsNode.isArray()) {
            for (JsonNode p : pointsNode) {
                double x = p.get("x").asDouble();
                double y = p.get("y").asDouble();
                mercatorPoints.add(new double[]{x, y});
            }
        }
        // 坐标转换: Web Mercator → WGS84
        List<double[]> wgs84List = new ArrayList<>();
        List<double[]> cesiumList = new ArrayList<>();
        for (double[] p : mercatorPoints) {
            double[] wgs84 = mercatorToWgs84(p[0], p[1]);
            wgs84List.add(wgs84);
            cesiumList.add(new double[]{wgs84[0], wgs84[1], 0});
        }
        result.setCoordinates(wgs84List);
        result.setCesiumPositions(cesiumList);
        result.setPointCount(wgs84List.size());
        log.info("路径提取: {} 个点, {} 米", result.getPointCount(), result.getTotalDistance());

        return result;
    }

    /**
     * Web Mercator → WGS84
     */
    private double[] mercatorToWgs84(double x, double y) {
        double lon = (x / ORIGIN_SHIFT) * 180.0;
        double lat = (y / ORIGIN_SHIFT) * 180.0;
        lat = 180.0 / Math.PI * (2.0 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);

        return new double[]{
                Math.round(lon * 1e6) / 1e6,
                Math.round(lat * 1e6) / 1e6
        };
    }
}