package com.test.digitalcampus.pojo;

import lombok.Data;
import lombok.NonNull;

@Data
public class Course
{
    @NonNull
    String CourseName;
    String Building;
    String ClassRoom;
    String Teacher;
    String WeekDay;
    String BeginTime;
    String EndTime;
}
