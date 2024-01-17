package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id; // -уникальный идентификатор вещи
    private String name; // краткое название
    private String description; // - развернутое описание
    private Boolean available; // - статус о том, доступна или нет вещь для аренды
    private Integer owner; // -влделец вещи
    private Integer request; // если вещь была создана по запросу другого пользоватлея, то в этом поле будет хранится
    // ссылка на соответсвующий запрос
}