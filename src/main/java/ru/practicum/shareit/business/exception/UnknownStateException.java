package ru.practicum.shareit.business.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String message) {
        super(message);
        log.warn(message);
    }
}