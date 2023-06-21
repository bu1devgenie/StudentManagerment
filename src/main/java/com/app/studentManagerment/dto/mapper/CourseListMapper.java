package com.app.studentManagerment.dto.mapper;

import com.app.studentManagerment.dto.CourseDto;
import com.app.studentManagerment.dto.mapper.decorrator.CourseListMapperDecorator;
import com.app.studentManagerment.entity.Course;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
@DecoratedWith(CourseListMapperDecorator.class)
public interface CourseListMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "courseSemester", target = "courseSemester")
    @Mapping(source = "totalSlot", target = "totalSlot")
    CourseDto courseToCourseDto(Course course);

}
