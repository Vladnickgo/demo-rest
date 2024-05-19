package com.example.demo.service.mapper.mapperImpl;

import com.example.demo.entities.User;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User mapDtoToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress())
                .birthDate(userDto.getBirthDate())
                .build();
    }

    @Override
    public UserDto mapEntityToDto(User user) {
        return
                UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phoneNumber(user.getPhoneNumber())
                        .address(user.getAddress())
                        .birthDate(user.getBirthDate())
                        .build();
    }
}
