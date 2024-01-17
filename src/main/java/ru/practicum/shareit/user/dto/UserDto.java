package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private final int id;
    @NotBlank
    private final String name;
    @NotBlank(message = "почта не может быть пустой")
    @Email(message = "не корректно указанная почта")
    private final String email;
}