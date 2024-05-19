package com.example.demo.repository.impl;

import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String SAVE = "INSERT INTO users(address, birth_date, email, first_name, last_name, phone_number) " +
            "VALUES (?, ?, ?, ?, ?, ?); ";
    private static final String FIND_BY_BIRTH_DATE_RANGE = "SELECT * FROM users WHERE birth_date BETWEEN ? AND ? LIMIT ? OFFSET ?;";
    private static final String COUNT_USERS_BY_BIRTH_DATE_RANGE = "SELECT COUNT(*) FROM users WHERE birth_date BETWEEN ? AND ?;";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";

    public static final String UPDATE_BY_ID = "UPDATE users " +
            "SET address=?, birth_date=?, email=?, first_name=?, last_name=?, phone_number=? " +
            "WHERE id = ?; ";

    public static final String DELETE_BY_ID = "DELETE FROM users WHERE id = ?;";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(SAVE, ps -> setPreparedStatement(user, ps));
    }

    @Override
    public void update(Integer id, User updatedUser) {
        jdbcTemplate.update(UPDATE_BY_ID, ps -> {
            setPreparedStatement(updatedUser, ps);
            ps.setInt(7, id);
        });
    }

    private static void setPreparedStatement(User updatedUser, PreparedStatement ps) throws SQLException {
        ps.setString(1, updatedUser.getAddress());
        ps.setDate(2, Date.valueOf(updatedUser.getBirthDate()));
        ps.setString(3, updatedUser.getEmail());
        ps.setString(4, updatedUser.getFirstName());
        ps.setString(5, updatedUser.getLastName());
        ps.setString(6, updatedUser.getPhoneNumber());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(DELETE_BY_ID, ps -> ps.setInt(1, id));
    }

    @Override
    public List<User> findByBirthDayRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int offset = pageNumber * pageSize;
        return jdbcTemplate.query(FIND_BY_BIRTH_DATE_RANGE, ps -> {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ps.setInt(3, pageSize);
            ps.setInt(4, offset);
        }, (rs, rowNum) -> User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .birthDate(rs.getDate("birth_date").toLocalDate())
                .address(rs.getString("address"))
                .phoneNumber(rs.getString("phone_number"))
                .build());
    }

    public Integer countByBirthDateBetween(LocalDate fromDate, LocalDate toDate) {
        return jdbcTemplate.queryForObject(COUNT_USERS_BY_BIRTH_DATE_RANGE, new Object[]{
                Date.valueOf(fromDate), Date.valueOf(toDate)
        }, Integer.class);
    }

    public Optional<User> findById(Integer id) {
        try {
            User user = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, (rs, rowNum) -> User.builder()
                    .id(rs.getInt("id"))
                    .email(rs.getString("email"))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .phoneNumber(rs.getString("phone_number"))
                    .birthDate(rs.getDate("birth_date").toLocalDate())
                    .build());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
