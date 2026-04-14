package com.test.digitalcampus.mapper;

import com.test.digitalcampus.pojo.Course;
import com.test.digitalcampus.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalTime;
import java.util.List;

@Mapper
public interface StuMapper {



    @Select( "Select *"+
            "FROM course_choice cc " +
            "JOIN course cs ON cc.course_name = cs.course_name " +
            "WHERE cc.stu_id = #{stuId}")
    List<Course> getCours(String stuID);

    @Select("select * from student where id=#{id}")
    Student getByid(String id);

    @Select("select * from course where building=#{building} and classroom=#{classroom}")
    List<Course> getClassroomcourse(String building, String classroom);



    List<Course> getClassroom(@Param("building") String building,
                              @Param("beginTime") LocalTime beginTime,
                              @Param("endTime") LocalTime endTime,
                              @Param("weekDay") String weekDay);
}
