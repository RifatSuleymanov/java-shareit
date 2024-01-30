package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByBookerOrderByStartDesc(User booker, Pageable pageable); // ALL

    Page<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable); // CURRENT

    Page<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(
            User booker, LocalDateTime end, Pageable pageable); // PAST

    Page<Booking> findAllByBookerAndStartAfterOrderByStartDesc(
            User booker, LocalDateTime start, Pageable pageable);  //FUTURE

    Page<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(
            User booker, BookingStatus status, Pageable pageable); //WAITING / REJECTED


    Page<Booking> findAllByItemOwnerOrderByStartDesc(
            User owner, Pageable pageable); // ALL

    Page<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end, Pageable pageable); // CURRENT

    Page<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(
            User owner, LocalDateTime end, Pageable pageable); // PAST

    Page<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(
            User owner, LocalDateTime start, Pageable pageable);  //FUTURE

    Page<Booking> findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
            User owner, BookingStatus status, Pageable pageable); //WAITING / REJECTED

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
            int itemId, BookingStatus status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
            int itemId, BookingStatus status, LocalDateTime dateTime);

    Boolean existsByBookerIdAndItemIdAndStartIsBefore(
            int userId, int itemId, LocalDateTime time);
}
