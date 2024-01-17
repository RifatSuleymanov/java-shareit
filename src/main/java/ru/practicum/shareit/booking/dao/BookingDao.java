package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface BookingDao {

    Booking addBooking(Booking booking); // Добавление нового запроса бронирование

    Booking responseToRequest(Booking booking, boolean answer); // Потверждение или отклонение запроса на бронирование

    Booking getInfoBooking(int id, int userId); // Получение данных о конкретном бронировании

    List<Booking> getAllBookingOneUser(User user, String state); // Получение списка всеъ бронирований текущего пользователя

    List<Booking> getAllBookingOneOwner(User user, String state); // получение списка бронирований для всех вещей текущего пользователя

    Booking getBookingById(Integer id);

    Optional<Booking> getLast(int id);

    Optional<Booking> getNext(int id);

    void checkUserBooking(Integer userId, Integer itemId);


}