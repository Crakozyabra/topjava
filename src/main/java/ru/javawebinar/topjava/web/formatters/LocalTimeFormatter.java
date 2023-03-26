package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    private static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, LOCAL_TIME_FORMATTER);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return String.valueOf(localTime);
    }
}
