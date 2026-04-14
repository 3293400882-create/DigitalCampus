package com.test.digitalcampus.pojo;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PathRequest {
    @NotNull
    private Double x;
    @NotNull
    private Double y;
}
