package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.services.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String OWNER_ID = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody InputBookingDto inputBookingDto, @RequestHeader(OWNER_ID) Integer userId) { // Добавление нового запроса на бронирование.
        log.info("метод addBooking userId " + userId);
        return bookingService.addBooking(inputBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto responseToRequest(@PathVariable Integer bookingId, @RequestHeader(OWNER_ID) Integer userId,
                                        @RequestParam Boolean approved) { // Подтверждение или отклонение запроса на бронирование.
        log.info("метод responseToRequest userId " + userId + "bookingId" + bookingId);
        return bookingService.responseToRequest(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getInfoBooking(@PathVariable Integer bookingId, @RequestHeader(OWNER_ID) Integer userId) { // Получение данных о конкретном бронировании
        log.info("метод getInfoBooking userId " + userId + "bookingId" + bookingId);
        return bookingService.getInfoBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingOneUser(@RequestHeader(OWNER_ID) Integer userId,
                                                 @RequestParam(defaultValue = "ALL") String state) { // Получение списка всех бронирований текущего пользователя.
        log.info("метод getAllBookingOneUser userId " + userId);
        return bookingService.getAllBookingOneUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingOneOwner(@RequestHeader(OWNER_ID) Integer userId,
                                                  @RequestParam(defaultValue = "ALL") String state) { // Получение списка бронирований для всех вещей текущего пользователя.
        log.info("метод getAllBookingOneOwner userId " + userId);
        return bookingService.getAllBookingOneOwner(userId, state);

    }
}