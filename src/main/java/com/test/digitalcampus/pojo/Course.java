package com.test.digitalcampus.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalTime;

@Data
public class Course
{
    @NotNull
    String CourseName;
    String Building;
    String ClassRoom;
    String Teacher;
    String WeekDay;

    LocalTime BeginTime;
    LocalTime  EndTime;
}
