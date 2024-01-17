package ru.practicum.shareit.exception.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    String error;
    String description;

    public String getDescription() {
        return description;
    }
}