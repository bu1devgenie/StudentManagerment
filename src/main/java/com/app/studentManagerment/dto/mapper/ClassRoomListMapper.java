package com.app.studentManagerment.dto.mapper;

import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.dto.mapper.decorrator.ClassRoomListMapperDecorator;
import com.app.studentManagerment.entity.ClassRoom;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TeacherListMapper.class, StudentListMapper.class})
@DecoratedWith(ClassRoomListMapperDecorator.class)
public interface ClassRoomListMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "semester", source = "semester")
    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "course", source = "course")
    @Mapping(target = "currentSlot", source = "currentSlot")
    @Mapping(target = "learning", source = "learning")
    @Mapping(target = "students", source = "students")
    ClassRoomDto classRoomToclassRoomDto(ClassRoom classRoom);
}
