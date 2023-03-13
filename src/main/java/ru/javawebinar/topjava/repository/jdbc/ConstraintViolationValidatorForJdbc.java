package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public class ConstraintViolationValidatorForJdbc<T> {

    private final static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public void validate(T t) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
