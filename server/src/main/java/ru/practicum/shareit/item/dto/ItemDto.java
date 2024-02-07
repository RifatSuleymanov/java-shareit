package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class ItemDto {
    private int id; // уникальный идентификатор вещи
    private String name; // краткое название
    private String description; // развернутое описание
    private Boolean available; // статус о том, доступна ли этот вещь для аренты
    private List<CommentDto> comments;
    private InputBookingDto lastBooking;
    private InputBookingDto nextBooking;
    private Integer requestId;

}