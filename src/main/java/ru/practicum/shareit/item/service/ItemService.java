package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {
    public final ItemDao itemDao;
    private final UserDao userDao;
    private final BookingDao bookingDao;
    private final RequestDao requestDao;

    @Transactional
    public ItemDto addItems(ItemDto itemDto, int ownerId) {
        userDao.getUserById(ownerId);
        Item item = ItemMapper.toItem(itemDto, userDao.getUserById(ownerId));
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestDao.getRequestById(itemDto.getRequestId()));
        }
        return ItemMapper.toItemDto(itemDao.addItems(item));
    }

    @Transactional
    public ItemDto updateItems(int itemId, ItemDto itemDto, int ownerId) {
        Item item = ItemMapper.toItem(itemDto, userDao.getUserById(ownerId));
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestDao.getRequestById(itemDto.getRequestId()));
        }
        return ItemMapper.toItemDto(itemDao.updateItem(itemId, item));
    }

    @Transactional(readOnly = true)
    public ItemDto getItemsById(int itemId, int ownerId) {
        Item item = itemDao.getItemById(itemId);
        ItemDto dto = ItemMapper.toItemDto(item);
        setDtoComments(dto);
        if (ownerId == item.getOwner().getId()) {
            setDtoNextAndLast(dto);
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsOneUser(int ownerId, int from, int size) {
        List<Item> items = itemDao.getAllItemsOneUser(ownerId, from, size);

        return items.stream()
                .map(ItemMapper::toItemDto)
                .map(this::setDtoComments)
                .map(this::setDtoNextAndLast)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ItemDto> searchItemByText(String text, int from, int size) {
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        return itemDao.searchItemByText(text, from, size)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(int itemId, int userId, CommentDto commentDto) {
        bookingDao.checkUserBooking(userId, itemId);
        Item item = itemDao.getItemById(itemId);
        User user = userDao.getUserById(userId);
        Comment comment = CommentMapper.toComment(commentDto, user, item);
        return CommentMapper.toCommentDto(itemDao.addComment(comment));
    }

    private ItemDto setDtoComments(ItemDto dto) {
        List<Comment> comments = itemDao.getAllCommentOneItem(dto.getId());
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentDto.add(CommentMapper.toCommentDto(comment));
        }
        dto.setComments(commentDto);
        return dto;
    }

    public ItemDto setDtoNextAndLast(ItemDto dto) {
        Optional<Booking> lastBookingOpt = bookingDao.getLast(dto.getId());
        Optional<Booking> nextBookingOpt = bookingDao.getNext(dto.getId());

        dto.setLastBooking(lastBookingOpt.map(BookingMapper::toInputBookingDto).orElse(null));
        dto.setNextBooking(nextBookingOpt.map(BookingMapper::toInputBookingDto).orElse(null));

        return dto;
    }
}