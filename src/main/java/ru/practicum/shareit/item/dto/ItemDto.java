package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public final class ItemDto {
    private final int id; // уникальный идентификатор вещи
    @NotBlank
    private final String name; // краткое название
    @NotBlank
    private final String description; // развернутое описание
    @NotNull
    private final Boolean available; // статус о том, доступна ли этот вещь для аренты
    private final Integer request; // если вещь была создана по запросу другого пользователя, то в этом поле будет
    // хранится ссылка на соответсвующий запрос
}