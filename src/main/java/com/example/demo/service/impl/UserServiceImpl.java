package com.example.demo.service.impl;

import com.example.demo.entities.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.impl.UserRepositoryImpl;
import com.example.demo.service.UserService;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.mapper.mapperImpl.UserMapperImpl;
import com.example.demo.service.validator.validatorImpl.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.exception.ExceptionMessage.FROM_DATE_MORE_THAN_TO_DATE;
import static com.example.demo.exception.ExceptionMessage.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;
    private final UserValidator userValidator;
    private final UserMapperImpl userMapper;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository, UserValidator userValidator, UserMapperImpl userMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto save(UserDto userDto) {
        userValidator.validate(userDto);
        User user = userMapper.mapDtoToEntity(userDto);
        userRepository.save(user);
        return userDto;
    }

    @Override
    public Page<UserDto> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(FROM_DATE_MORE_THAN_TO_DATE );
        }
        List<User> userList = userRepository.findByBirthDayRange(fromDate, toDate, pageable);
        List<UserDto> userDtoList = userList.stream().map(userMapper::mapEntityToDto).toList();
        Integer total = countByBirthDateBetween(fromDate, toDate);
        return new PageImpl<>(userDtoList, pageable, total);
    }


    @Override
    public void updateCompletely(Integer id, UserDto userDto) {
        userValidator.validate(userDto);
        User user = userMapper.mapDtoToEntity(userDto);
        userRepository.update(id, user);
    }

    @Override
    public UserDto updatePartially(Integer id, UserDto userDto) {
        User newUserData = userMapper.mapDtoToEntity(userDto);
        User oldUserData = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, id)));
        newUserData = User.builder()
                .id(id)
                .firstName(newUserData.getFirstName() == null ? oldUserData.getFirstName() : newUserData.getFirstName())
                .lastName(newUserData.getLastName() == null ? oldUserData.getLastName() : newUserData.getLastName())
                .email(newUserData.getEmail() == null ? oldUserData.getEmail() : newUserData.getEmail())
                .phoneNumber(newUserData.getPhoneNumber() == null ? oldUserData.getPhoneNumber() : newUserData.getPhoneNumber())
                .birthDate(newUserData.getBirthDate() == null ? oldUserData.getBirthDate() : newUserData.getBirthDate())
                .address(newUserData.getAddress() == null ? oldUserData.getAddress() : newUserData.getAddress())
                .build();
        userRepository.update(id, newUserData);
        return userMapper.mapEntityToDto(newUserData);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.delete(id);
    }

    private Integer countByBirthDateBetween(LocalDate fromDate, LocalDate toDate) {
        return userRepository.countByBirthDateBetween(fromDate, toDate);
    }
}
