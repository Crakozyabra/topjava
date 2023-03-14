package ru.javawebinar.topjava.util;

import javax.validation.*;
import java.util.Set;

public class ConstraintViolationValidator {

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public <T> void validate(T t) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
