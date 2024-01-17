package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private int id; // уникальный идентификатор вещи
    @NotBlank
    private String name; // краткое название
    @NotBlank
    private String description; // развернутое описание
    @NotNull
    private Boolean available; // статус о том, доступна ли этот вещь для аренты
    private Integer request; // если вещь была создана по запросу другого пользователя, то в этом поле будет
    // хранится ссылка на соответсвующий запрос
}