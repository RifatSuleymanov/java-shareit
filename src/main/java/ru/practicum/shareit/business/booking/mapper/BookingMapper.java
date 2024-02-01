package ru.practicum.shareit.business.booking.mapper;

import ru.practicum.shareit.business.booking.dto.BookingDto;
import ru.practicum.shareit.business.booking.dto.InputBookingDto;
import ru.practicum.shareit.business.booking.model.Booking;
import ru.practicum.shareit.business.exception.BadRequestException;
import ru.practicum.shareit.business.item.model.Item;
import ru.practicum.shareit.business.user.model.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItem(booking.getItem());
        dto.setBooker(booking.getBooker());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static Booking toBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(dto.getItem());
        booking.setBooker(dto.getBooker());
        booking.setStatus(dto.getStatus());
        return booking;
    }

    public static Booking fromInputBookingDtoToBooking(InputBookingDto inputBookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setId(inputBookingDto.getId());
        booking.setStart(inputBookingDto.getStart());
        booking.setEnd(inputBookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            throw new BadRequestException("ошибка в дате аренды");
        }
        return booking;
    }

    public static InputBookingDto toInputBookingDto(Booking booking) {
        InputBookingDto bookingDto = new InputBookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setItemId(booking.getItem().getId());
        return bookingDto;
    }
}