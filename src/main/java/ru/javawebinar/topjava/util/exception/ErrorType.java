package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorType {
    APP_ERROR("Ошибка приложения"),
    DATA_NOT_FOUND("Данные не найдены"),
    DATA_ERROR("Ошибка данных"),
    VALIDATION_ERROR("Ошибка проверки данных");

    private String name;

    ErrorType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
