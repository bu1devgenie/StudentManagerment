package com.app.studentManagerment.dto.mapper.decorrator;

import com.app.studentManagerment.dto.mapper.ClassRoomListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ClassRoomListMapperDecorator implements ClassRoomListMapper {
    @Autowired
    @Qualifier("delegate")
    private ClassRoomListMapper classRoomListMapper;


}
