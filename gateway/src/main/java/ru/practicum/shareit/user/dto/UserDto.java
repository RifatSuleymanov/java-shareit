package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class UserDto {
    @NotBlank
    private String name;
    @NotBlank(message = "почта не может быть пустой")
    @Email(message = "не корректно указанная почта")
    private String email;
}