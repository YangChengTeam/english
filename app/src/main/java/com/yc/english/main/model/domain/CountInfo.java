package com.yc.english.main.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/8/11.
 */

public class CountInfo {
    @JSONField(name = "teacher_count")
    private String teacherCount;

    @JSONField(name = "student_count")
    private String studentCount;

    public String getTeacherCount() {
        return teacherCount;
    }

    public void setTeacherCount(String teacherCount) {
        this.teacherCount = teacherCount;
    }

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }
}
