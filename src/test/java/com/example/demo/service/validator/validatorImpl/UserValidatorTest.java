package com.example.demo.service.validator.validatorImpl;

import com.example.demo.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.demo.config.ApplicationConstants.MINIMAL_AGE;
import static com.example.demo.service.validator.ValidatorErrorMessage.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorTest {
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void validate_shouldThrowException_whenUserDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(null), USER_IS_NULL_MESSAGE);
    }

    @Test
    void validate_shouldThrowException_whenEmailIsNull() {
        UserDto userDto = UserDto.builder().firstName("John").lastName("Doe").birthDate(LocalDate.of(2000, 1, 1)).build();
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(userDto), EMAIL_IS_NULL);
    }

    @Test
    void validate_shouldThrowException_whenFirstNameIsNull() {
        UserDto userDto = UserDto.builder().email("test@example.com").lastName("Doe").birthDate(LocalDate.of(2000, 1, 1)).build();
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(userDto), FIRST_NAME_IS_NULL);
    }

    @Test
    void validate_shouldThrowException_whenLastNameIsNull() {
        UserDto userDto = UserDto.builder().email("test@example.com").firstName("John").birthDate(LocalDate.of(2000, 1, 1)).build();
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(userDto), LAST_NAME_IS_NULL);
    }

    @Test
    void validate_shouldThrowException_whenBirthDateIsNull() {
        UserDto userDto = UserDto.builder().email("test@example.com").firstName("John").lastName("Doe").build();
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(userDto), BIRTH_DATE_IS_NULL);
    }

    @Test
    void validate_shouldThrowException_whenEmailIsInvalid() {
        UserDto userDto = UserDto.builder()
                .email("invalid-email")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(userDto), EMAIL_ERROR_MESSAGE);
    }

    @Test
    void validate_shouldThrowException_whenUserIsUnderage() {
        UserDto userDto = UserDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.now().minusYears(MINIMAL_AGE - 1))
                .build();
        assertThrows(IllegalArgumentException.class, () -> userValidator.validate(userDto), USER_AGE_ERROR_MESSAGE);
    }

    @Test
    void validate_shouldPass_whenUserDtoIsValid() {
        UserDto userDto = UserDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
        userValidator.validate(userDto);
    }
}