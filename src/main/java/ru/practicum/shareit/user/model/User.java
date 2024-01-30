package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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