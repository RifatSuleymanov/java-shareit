package ru.practicum.shareit.business.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.business.booking.model.BookingStatus;
import ru.practicum.shareit.business.item.model.Item;
import ru.practicum.shareit.business.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class BookingDto {

    private int id;

    @NotNull(message = "not NULL")
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(message = "not NULL")
    @Future
    private LocalDateTime end;

    private User booker;
    private Item item;
    private BookingStatus status = BookingStatus.WAITING;
}