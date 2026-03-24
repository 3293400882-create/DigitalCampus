package com.test.digitalcampus.pojo;

import lombok.Data;
import lombok.NonNull;

@Data
public class Student {

    @NonNull
    String StuName;
    String ID;
    String StuPassword;
}
