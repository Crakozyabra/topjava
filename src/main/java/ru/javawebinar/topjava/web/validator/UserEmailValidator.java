package ru.javawebinar.topjava.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Objects;

@Component
public class UserEmailValidator implements Validator {

    private UserService userService;

    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz) || User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Integer userId = getUserId(target);
        String email = getEmail(target);
        if (Objects.isNull(userId)) {
            AuthorizedUser authorizedUser = SecurityUtil.safeGet();
            if (Objects.nonNull(authorizedUser)) {
                userId = authorizedUser.getId();
            }
        }
        if (Objects.isNull(errors.getFieldError("email"))) {
            try {
                User saved = userService.getByEmail(email);
                if (!Objects.equals(userId, saved.getId())) {
                    errors.rejectValue("email", "error.user.email.duplicate", "error.user.email.duplicate");
                }
            } catch (NotFoundException e) {
            }
        }
    }

    private Integer getUserId(Object target) {
        if (target instanceof UserTo) {
            return ((UserTo) target).getId();
        }
        return ((User) target).getId();
    }

    private String getEmail(Object target) {
        if (target instanceof UserTo) {
            return ((UserTo) target).getEmail();
        }
        return ((User) target).getEmail();
    }
}
