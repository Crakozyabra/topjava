package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        if (Objects.isNull(localTime)) return "null";
        return localTime.toString();
    }
}
