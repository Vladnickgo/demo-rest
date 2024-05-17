package com.example.demo.service.validator;

public interface Validator<E> {
    void validate(E entity);
}
