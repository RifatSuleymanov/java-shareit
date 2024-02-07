package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Идентификатор пользователя
    @Column(length = 50, nullable = false)
    private String name; // Имя пользователя
    @Column(unique = true, length = 50, nullable = false)
    private String email; // Email пользователя
}