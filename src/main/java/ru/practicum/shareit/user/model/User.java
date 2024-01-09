package ru.practicum.shareit.user.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {
    private int id; // Идентификатор пользователя
    private String name; // Имя пользователя
    private String email; // Email пользователя
}