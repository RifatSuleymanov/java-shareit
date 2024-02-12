package ru.practicum.shareit.booking.services;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(InputBookingDto inputBookingDto, Integer userId);

    BookingDto responseToRequest(int bookingId, int userId, Boolean answer);

    BookingDto getInfoBooking(int bookingId, int userId);

    List<BookingDto> getAllBookingOneUser(int userId, String state, int from, int size);

    List<BookingDto> getAllBookingOneOwner(int userId, String state, int from, int size);

}
