package com.app.studentManagerment.dto.mapper.decorrator;

import com.app.studentManagerment.dto.CourseDto;
import com.app.studentManagerment.dto.mapper.CourseListMapper;
import com.app.studentManagerment.entity.Course;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CourseListMapperDecorator implements CourseListMapper {
    @Autowired
    @Qualifier("delegate")
    private CourseListMapper courseListMapper;

}
