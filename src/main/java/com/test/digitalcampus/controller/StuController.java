package com.test.digitalcampus.controller;


import com.test.digitalcampus.pojo.Classroom;
import com.test.digitalcampus.pojo.Course;
import com.test.digitalcampus.pojo.Result;
import com.test.digitalcampus.pojo.Student;
import com.test.digitalcampus.service.StuService;

import com.test.digitalcampus.utils.JwtUtil;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import com.test.digitalcampus.mapper.StuMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class StuController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private  StuService stuService;

    @PostMapping("login")
    public Result login(String StuID, String Stupassword){

        Student stu= stuService.getByid(StuID);


        if(stu!=null){


            if (stu.getStuPassword().equals(Stupassword)){
                Map<String,Object> claims=new HashMap<>();
                claims.put("id",stu.getID());
                claims.put("StuName",stu.getStuName());
                String token = JwtUtil.genToken(claims);
                //将令牌存储至redis,失效时间设置为1天
                ValueOperations<String,String> ops = stringRedisTemplate.opsForValue();
                ops.set(token,token,1, TimeUnit.DAYS);
                return Result.success("登录成功",token);
            }

            return Result.error("密码错误，请重试");

        }

        return Result.error("暂无此人，请联系学院");
    }

    @GetMapping("course")
    public Result<List<Course>> getCourse() {
         List<Course> courses= stuService.getCourse();
         if(courses == null || courses.isEmpty()){
             return Result.error("暂无数据");
         }
         return Result.success(courses);
    }
    @PostMapping("classcourse")
    public Result<List<Course>> getclassroomCourse(String building,String classroom) {
        List<Course> courses= stuService.getClassroomCourse(building,classroom);
        if(courses == null || courses.isEmpty()){
            return Result.error("暂无数据");
        }
        return Result.success(courses);
    }


}
