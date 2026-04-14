package com.test.digitalcampus.service;


import com.test.digitalcampus.pojo.Course;
import com.test.digitalcampus.pojo.Student;

import java.time.LocalTime;
import java.util.List;


public interface StuService {

    List<Course> getCourse();

    Student getByid(String id);

    List<Course> getClassroomCourse(String building, String classroom);

    List<Course> getClassroom(String building, LocalTime beginTime, LocalTime endTime, String weekDay);
}
