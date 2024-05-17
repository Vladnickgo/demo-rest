package com.example.demo.service;

import com.example.demo.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {
    UserDto save(UserDto userDto);

    Page<UserDto> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable);

    void updateCompletely(Integer id, UserDto userDto);

    UserDto updatePartially(Integer id, UserDto userDto);

    void deleteById(Integer id);
}
