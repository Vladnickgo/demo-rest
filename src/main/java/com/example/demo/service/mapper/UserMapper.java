package com.example.demo.service.mapper;

import com.example.demo.entities.User;
import com.example.demo.service.dto.UserDto;

public interface UserMapper {
    User mapDtoToEntity(UserDto userDto);

    UserDto mapEntityToDto(User user);
}
