package com.example.demo.service.validator.validatorImpl;

import com.example.demo.service.dto.UserDto;
import com.example.demo.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.Function;

import static com.example.demo.config.ApplicationConstants.MINIMAL_AGE;
import static com.example.demo.service.validator.ValidatorErrorMessage.*;
import static io.micrometer.common.util.StringUtils.isBlank;

@Component
public class UserValidator implements Validator<UserDto> {
    @Override
    public void validate(UserDto entity) {
        if (entity == null) {
            throw new IllegalArgumentException(USER_IS_NULL_MESSAGE);
        }
        validateNotNull(UserDto::getEmail, EMAIL_IS_NULL, entity);
        validateNotNull(UserDto::getFirstName, FIRST_NAME_IS_NULL, entity);
        validateNotNull(UserDto::getLastName, LAST_NAME_IS_NULL, entity);
        validateNotNull(UserDto::getBirthDate, BIRTH_DATE_IS_NULL, entity);
        emailValidate(entity.getEmail());
        ageValidate(entity.getBirthDate());
    }

    private void validateNotNull(Function<UserDto, Object> param, String errorMessage, UserDto userDto) {
        Optional.ofNullable(param.apply(userDto)).orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private void emailValidate(String email) {
        if (isBlank(email) || !email.matches(Patterns.REGEX_FOR_EMAIL)) {
            throw new IllegalArgumentException(EMAIL_ERROR_MESSAGE);
        }
    }

    private void ageValidate(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        int years = period.getYears();
        if (years < MINIMAL_AGE) {
            throw new IllegalArgumentException(USER_AGE_ERROR_MESSAGE);
        }

    }
}
