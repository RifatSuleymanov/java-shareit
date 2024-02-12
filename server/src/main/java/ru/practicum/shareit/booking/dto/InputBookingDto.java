package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class InputBookingDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer bookerId;
    private Integer itemId;
}