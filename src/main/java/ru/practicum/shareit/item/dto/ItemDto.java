package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public final class ItemDto {
    private int id; // уникальный идентификатор вещи
    @NotBlank
    private String name; // краткое название
    @NotBlank
    private String description; // развернутое описание
    @NotNull
    private Boolean available; // статус о том, доступна ли этот вещь для аренты
    private List<CommentDto> comments;
    private InputBookingDto lastBooking;
    private InputBookingDto nextBooking;

}