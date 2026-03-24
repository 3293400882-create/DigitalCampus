package com.test.digitalcampus.service.impl;

import com.test.digitalcampus.mapper.StuMapper;
import com.test.digitalcampus.pojo.Course;
import com.test.digitalcampus.pojo.Student;
import com.test.digitalcampus.service.StuService;
import com.test.digitalcampus.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StuServiceImpl implements StuService {

    @Autowired
    private StuMapper stuMapper ;

    @Override
    public List<Course> getCourse() {

        Map<String,Object> map = ThreadLocalUtil.get();
        String stuID = map.get("id").toString();
        //先从jwt中获取id，然后导入得到课程列表
        List<Course>courses= stuMapper.getCours(stuID);
        return courses;
    }

    @Override
    public Student getByid(String id) {
       Student stu= stuMapper.getByid(id);
       return stu;
    }

    @Override
    public List<Course> getClassroomCourse(String building, String classroom) {
        List<Course>courses= stuMapper.getClassroomcourse(building,classroom);
        return courses;
    }
}
