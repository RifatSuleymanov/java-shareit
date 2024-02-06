package ru.practicum.shareit.booking.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BookingDaoImpl implements BookingDao {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Booking addBooking(Booking booking) {
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking responseToRequest(Booking booking, boolean answer) {
        if (answer) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking getInfoBooking(int id, int userId) {
        Booking booking = getBookingById(id);
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("вы не можете смотреть чужие запросы");
        }
        return booking;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllBookingOneUser(User user, String state, int from, int size) {
        Page<Booking> bookingList;
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByBookerOrderByStartDesc(user, page);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        user, BookingStatus.WAITING, page);
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        user, BookingStatus.REJECTED, page);
                break;
            default:
                throw new UnknownStateException("Unknown state:" + BookingStatus.UNSUPPORTED_STATUS);
        }
        return bookingList
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllBookingOneOwner(User user, String state, int from, int size) {
        Page<Booking> bookingList;
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByItemOwnerOrderByStartDesc(user, page);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(
                        user, LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                        user, BookingStatus.WAITING, page);
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByItemOwnerAndStatusEqualsOrderByStartDesc(
                        user, BookingStatus.REJECTED, page);
                break;
            default:
                throw new UnknownStateException("Неизвестный параметр " + state);
        }
        return bookingList
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("такова запроса нет"));
    }

    @Override
    public Optional<Booking> getLast(int id) {
        return bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                id, BookingStatus.APPROVED, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Booking> getNext(int id) {
        return bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                id, BookingStatus.APPROVED, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public void checkUserBooking(Integer userId, Integer itemId) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndStartIsBefore(userId, itemId, LocalDateTime.now())) {
            throw new BadRequestException("вы не можете оставлять комментарии на вещь которой не пользовались");
        }
    }

    @Override
    public Optional<Booking> findFirstByItemIdInAndStartLessThanEqualAndStatus(List<Integer> idItems, LocalDateTime now, BookingStatus approved, Sort sort) {
        return bookingRepository.findFirstByItemIdInAndStartLessThanEqualAndStatus(idItems, now, approved, sort);
    }

    @Override
    public Optional<Booking> findFirstByItemIdInAndStartAfterAndStatus(List<Integer> idItems, LocalDateTime now, BookingStatus approved, Sort sort) {
        return bookingRepository.findFirstByItemIdInAndStartAfterAndStatus(idItems, now, approved, sort);
    }
}