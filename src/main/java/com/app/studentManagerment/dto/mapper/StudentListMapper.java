package com.app.studentManagerment.dto.mapper;

import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.dto.mapper.decorrator.StudentListMapperDecorator;
import com.app.studentManagerment.entity.user.Student;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(StudentListMapperDecorator.class)
public interface StudentListMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "mssv", target = "mssv")
    @Mapping(source = "currentSemester", target = "currentSemester")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "avatar", target = "avatar")
    Student studentDtoToStudent(StudentDto studentDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "mssv", target = "mssv")
    @Mapping(source = "currentSemester", target = "currentSemester")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "avatar", target = "avatar")
    StudentDto StudentToStudentDto(Student student);


}
