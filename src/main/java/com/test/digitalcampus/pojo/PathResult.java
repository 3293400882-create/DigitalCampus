package com.test.digitalcampus.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PathResult {
    private double totalDistance;
    private List<double[]> coordinates;
    private List<double[]> cesiumPositions;
    private int pointCount;
}