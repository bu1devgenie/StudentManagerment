package com.app.studentManagerment.dto.mapper.decorrator;

import com.app.studentManagerment.dto.mapper.CourseListMapper;
import com.app.studentManagerment.dto.mapper.UserListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserListMapperDecorator implements UserListMapper {
    @Autowired
    @Qualifier("delegate")
    private UserListMapper userListMapper;
}
