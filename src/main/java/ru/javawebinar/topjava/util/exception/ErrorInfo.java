package ru.javawebinar.topjava.util.exception;

import java.util.Set;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final Set<String> details;

    public ErrorInfo(CharSequence url, ErrorType type, Set<String> details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }
}