package com.app.studentManagerment.dto.mapper.decorrator;

import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.dto.mapper.StudentListMapper;
import com.app.studentManagerment.entity.user.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

public abstract class StudentListMapperDecorator implements StudentListMapper {
    @Autowired
    @Qualifier("delegate")
    private StudentListMapper studentListMapper;

}
