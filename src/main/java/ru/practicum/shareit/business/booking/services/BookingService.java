package ru.practicum.shareit.business.booking.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.business.booking.dto.BookingDto;
import ru.practicum.shareit.businessInterface.BookingDao;
import ru.practicum.shareit.business.booking.dto.InputBookingDto;
import ru.practicum.shareit.business.booking.mapper.BookingMapper;
import ru.practicum.shareit.business.booking.model.Booking;
import ru.practicum.shareit.business.booking.model.BookingStatus;
import ru.practicum.shareit.business.exception.BadRequestException;
import ru.practicum.shareit.business.exception.NotFoundException;
import ru.practicum.shareit.businessInterface.ItemDao;
import ru.practicum.shareit.business.item.model.Item;
import ru.practicum.shareit.businessInterface.UserDao;
import ru.practicum.shareit.business.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    @Transactional
    public BookingDto addBooking(InputBookingDto inputBookingDto, Integer userId) {
        Item item = itemDao.getItemById(inputBookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new BadRequestException("предмет не доступен для аренды");
        } else if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("вы не можете брать в аренду свои вещи");
        }
        User user = userDao.getUserById(userId);
        Booking booking = BookingMapper.fromInputBookingDtoToBooking(inputBookingDto, item, user);
        return BookingMapper.toBookingDto(bookingDao.addBooking(booking));
    }

    @Transactional
    public BookingDto responseToRequest(int bookingId, int userId, Boolean answer) {
        BookingDto dto = BookingMapper.toBookingDto(bookingDao.getBookingById(bookingId));
        if (dto.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("вы не можете одобрять чужие заявки");
        } else if (!dto.getStatus().equals(BookingStatus.WAITING)) {
            throw new BadRequestException("Предмет уже забронирован");
        }
        return BookingMapper.toBookingDto(bookingDao.responseToRequest(BookingMapper.toBooking(dto), answer));
    }

    @Transactional(readOnly = true)
    public BookingDto getInfoBooking(int bookingId, int userId) {
        userDao.getUserById(userId);
        return BookingMapper.toBookingDto(bookingDao.getInfoBooking(bookingId, userId));
    }

    public List<BookingDto> getAllBookingOneUser(int userId, String state, int from, int size) {
        User user = userDao.getUserById(userId);
        if (from < 0) {
            throw new BadRequestException("from не может быть отрицательным");
        }
        return bookingDao.getAllBookingOneUser(user, state, from, size)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookingOneOwner(int userId, String state, int from, int size) {
        User user = userDao.getUserById(userId);
        if (from < 0) {
            throw new BadRequestException("from не может быть отрицательным");
        }
        return bookingDao.getAllBookingOneOwner(user, state, from, size)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
