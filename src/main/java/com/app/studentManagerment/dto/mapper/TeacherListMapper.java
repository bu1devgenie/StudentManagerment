package com.app.studentManagerment.dto.mapper;

import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.dto.mapper.decorrator.TeacherListMapperDecorator;
import com.app.studentManagerment.entity.user.Teacher;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CourseListMapper.class})
@DecoratedWith(TeacherListMapperDecorator.class)
public interface TeacherListMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "msgv", target = "msgv")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "course", target = "course")
    @Mapping(source = "gender", target = "gender")
    public TeacherDto teacherToTeacherDTO(Teacher teacher);
}
