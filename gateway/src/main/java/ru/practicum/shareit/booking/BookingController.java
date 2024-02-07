package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingClientDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.UnknownStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;
    private final String user = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingClientDto bookingClientDto, @RequestHeader(user) Integer userId) { // Добавление нового запроса на бронирование.
        log.info("метод addBooking userId " + userId);
        if (bookingClientDto.getStart().isAfter(bookingClientDto.getEnd()) ||
                bookingClientDto.getStart().equals(bookingClientDto.getEnd())) {
            throw new BadRequestException("ошибка в дате аренды");
        }
        return bookingClient.addBooking(bookingClientDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> responseToRequest(@PathVariable Integer bookingId, @RequestHeader(user) Integer userId,
                                                    @RequestParam Boolean approved) { // Подтверждение или отклонение запроса на бронирование.
        log.info("метод responseToRequest userId " + userId + "bookingId" + bookingId);
        return bookingClient.responseToRequest(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getInfoBooking(@PathVariable Integer bookingId, @RequestHeader(user) Integer userId) { // Получение данных о конкретном бронировании
        log.info("метод getInfoBooking userId " + userId + "bookingId" + bookingId);
        return bookingClient.getInfoBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingOneUser(@RequestHeader(user) Integer userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                       @Positive @RequestParam(defaultValue = "20", required = false) Integer size) { // Получение списка всех бронирований текущего пользователя.
        BookingState states = BookingState.from(state)
                .orElseThrow(() -> new UnknownStateException("Неизвестный параметр " + state));
        log.info("метод getAllBookingOneUser userId " + userId);
        return bookingClient.getAllBookingOneUser(userId, states, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingOneOwner(@RequestHeader(user) Integer userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                        @Positive @RequestParam(defaultValue = "20", required = false) Integer size) { // Получение списка бронирований для всех вещей текущего пользователя.
        BookingState states = BookingState.from(state)
                .orElseThrow(() -> new UnknownStateException("Неизвестный параметр " + state));
        log.info("метод getAllBookingOneOwner userId " + userId);
        return bookingClient.getAllBookingOneOwner(userId, states, from, size);

    }
}
