package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerOrderByStartDesc(User booker); // ALL

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end); // CURRENT

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(
            User booker, LocalDateTime end); // PAST

    List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(
            User booker, LocalDateTime start);  //FUTURE

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(
            User booker, BookingStatus status); //WAITING / REJECTED


    List<Booking> findAllByItemOwnerOrderByStartDesc(
            User owner); // ALL

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end); // CURRENT

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(
            User owner, LocalDateTime end); // PAST

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(
            User owner, LocalDateTime start);  //FUTURE

    List<Booking> findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
            User owner, BookingStatus status); //WAITING / REJECTED

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
            int itemId, BookingStatus status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
            int itemId, BookingStatus status, LocalDateTime dateTime);

    Boolean existsByBookerIdAndItemIdAndEndIsBefore(
            int userId, int itemId, LocalDateTime time);
}