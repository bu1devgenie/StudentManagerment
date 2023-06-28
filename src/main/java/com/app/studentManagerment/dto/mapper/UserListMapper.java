package com.app.studentManagerment.dto.mapper;

import com.app.studentManagerment.dto.UserDto;
import com.app.studentManagerment.dto.mapper.decorrator.UserListMapperDecorator;
import com.app.studentManagerment.entity.user.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(UserListMapperDecorator.class)
public interface UserListMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "gender", target = "gender")
    UserDto UserToUserDto(User user);
}
