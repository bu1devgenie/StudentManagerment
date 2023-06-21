package com.app.studentManagerment.dto.mapper.decorrator;

import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.dto.mapper.TeacherListMapper;
import com.app.studentManagerment.entity.user.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public abstract class TeacherListMapperDecorator implements TeacherListMapper {
    @Autowired
    @Qualifier("delegate")
    private TeacherListMapper TeacherListMapper;
}
