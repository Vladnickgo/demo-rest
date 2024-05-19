package com.example.demo.service.impl;


import com.example.demo.entities.User;
import com.example.demo.repository.impl.UserRepositoryImpl;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.mapper.mapperImpl.UserMapperImpl;
import com.example.demo.service.validator.validatorImpl.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.demo.exception.ExceptionMessage.FROM_DATE_MORE_THAN_TO_DATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserMapperImpl userMapper;
    @Mock
    UserRepositoryImpl userRepository;
    @Mock
    private UserValidator userValidator;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void save() {
        User user = User.builder()
                .id(1)
                .email("john@mail.com")
                .firstName("First")
                .lastName("Last")
                .address("Baker Str.")
                .phoneNumber("(333)123-45-67")
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
        UserDto userDto = UserDto.builder()
                .id(1)
                .email("john@mail.com")
                .firstName("First")
                .lastName("Last")
                .address("Baker Str.")
                .phoneNumber("(333)123-45-67")
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
        doNothing().when(userValidator).validate(userDto);
        when(userMapper.mapDtoToEntity(userDto)).thenReturn(user);
        doNothing().when(userRepository).save(user);
        userService.save(userDto);
        verify(userValidator, times(1)).validate(userDto);
        verify(userMapper, times(1)).mapDtoToEntity(userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findByBirthDateBetween() {
        LocalDate fromDate = LocalDate.parse("2000-01-01");
        LocalDate toDate = LocalDate.parse("2005-01-01");
        Pageable pageable = PageRequest.of(0, 10);
        User user = User.builder()
                .id(1)
                .email("john@mail.com")
                .firstName("First")
                .lastName("Last")
                .address("Baker Str.")
                .phoneNumber("(333)123-45-67")
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
        UserDto userDto = UserDto.builder()
                .id(1)
                .email("john@mail.com")
                .firstName("First")
                .lastName("Last")
                .address("Baker Str.")
                .phoneNumber("(333)123-45-67")
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
        List<User> userList = List.of(user);
        List<UserDto> userDtoList = List.of(userDto);
        when(userRepository.findByBirthDayRange(fromDate, toDate, pageable)).thenReturn(userList);
        when(userMapper.mapEntityToDto(user)).thenReturn(userDto);
        when(userRepository.countByBirthDateBetween(fromDate, toDate)).thenReturn(1);
        Page<UserDto> result = userService.findByBirthDateBetween(fromDate, toDate, pageable);
        assertEquals(new PageImpl<>(userDtoList, pageable, 1), result);
        verify(userRepository, times(1)).findByBirthDayRange(fromDate, toDate, pageable);
        verify(userMapper, times(1)).mapEntityToDto(user);
        verify(userRepository, times(1)).countByBirthDateBetween(fromDate, toDate);
    }

    @Test
    void findByBirthDateBetweenThrowsException() {
        LocalDate fromDate = LocalDate.of(2000, 12, 31);
        LocalDate toDate = LocalDate.of(2000, 1, 1);
        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.findByBirthDateBetween(fromDate, toDate, pageable));
        assertEquals(FROM_DATE_MORE_THAN_TO_DATE, exception.getMessage());
    }

    @Test
    void updateCompletely() {
        Integer id = 1;
        UserDto userDto = UserDto.builder()
                .id(id)
                .email("john@mail.com")
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .birthDate(LocalDate.of(2000, 5, 20))
                .build();

        User user = User.builder()
                .id(id)
                .email("john@mail.com")
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .birthDate(LocalDate.of(2000, 5, 20))
                .build();

        doNothing().when(userValidator).validate(userDto);
        when(userMapper.mapDtoToEntity(userDto)).thenReturn(user);
        doNothing().when(userRepository).update(id, user);

        userService.updateCompletely(id, userDto);

        verify(userValidator, times(1)).validate(userDto);
        verify(userMapper, times(1)).mapDtoToEntity(userDto);
        verify(userRepository, times(1)).update(id, user);
    }

    @Test
    void updatePartially() {
        Integer id = 1;
        UserDto userDto = UserDto.builder()
                .email("newemail@mail.com")
                .firstName(null)
                .lastName("NewLastName")
                .address(null)
                .phoneNumber("123-456-7890")
                .birthDate(LocalDate.of(1995, 5, 20))
                .build();
        User newUserData = User.builder()
                .email("newemail@mail.com")
                .firstName(null)
                .lastName("NewLastName")
                .address(null)
                .phoneNumber("123-456-7890")
                .birthDate(LocalDate.of(1995, 5, 20))
                .build();
        User oldUserData = User.builder()
                .id(id)
                .email("oldemail@mail.com")
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .address("Old Address")
                .phoneNumber("098-765-4321")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
        User mergedUserData = User.builder()
                .id(id)
                .email("newemail@mail.com")
                .firstName("OldFirstName")
                .lastName("NewLastName")
                .address("Old Address")
                .phoneNumber("123-456-7890")
                .birthDate(LocalDate.of(1995, 5, 20))
                .build();
        when(userMapper.mapDtoToEntity(userDto)).thenReturn(newUserData);
        when(userRepository.findById(id)).thenReturn(Optional.of(oldUserData));
        doNothing().when(userRepository).update(id, mergedUserData);
        when(userMapper.mapEntityToDto(mergedUserData)).thenReturn(userDto);
        UserDto result = userService.updatePartially(id, userDto);
        verify(userMapper, times(1)).mapDtoToEntity(userDto);
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).update(id, mergedUserData);
        verify(userMapper, times(1)).mapEntityToDto(mergedUserData);
        assertEquals(userDto, result);
    }

    @Test
    void deleteById() {
        Integer id = 1;
        doNothing().when(userRepository).delete(id);
        userService.deleteById(id);
        verify(userRepository, times(1)).delete(id);
    }
}