package edu.hogwarts.studentadmin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    private final HttpStatus error;
    private final ZonedDateTime timeStamp;

    public ApiException(String message, HttpStatus error, ZonedDateTime timeStamp) {
        this.message = message;
        this.error = error;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getError() {
        return error;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }
}
