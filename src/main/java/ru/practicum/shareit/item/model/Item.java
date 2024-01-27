package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // -уникальный идентификатор вещи
    @Column(length = 200, nullable = false)
    private String name; // краткое название
    @Column(length = 1000, nullable = false)
    private String description; // - развернутое описание
    @Column
    private Boolean available; // - статус о том, доступна или нет вещь для аренды
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // -влделец вещи
    private Integer request; // если вещь была создана по запросу другого пользоватлея, то в этом поле будет хранится
    // ссылка на соответсвующий запрос
}