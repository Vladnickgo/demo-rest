package com.example.demo.repository;

import com.example.demo.entities.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository {
    void save(User user);

    void update(Integer id, User updatedUser);

    void delete(Integer user);

    List<User> findByBirthDayRange(LocalDate startDate, LocalDate endDate, Pageable pageable);


}
