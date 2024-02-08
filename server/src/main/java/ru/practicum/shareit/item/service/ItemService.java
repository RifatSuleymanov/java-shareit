package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.request.dao.RequestDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class ItemService {
    public final ItemDao itemDao;
    private final UserDao userDao;
    private final BookingDao bookingDao;
    private final RequestDao requestDao;
    private final CommentRepository commentRepository;

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
        Page<Item> items = itemDao.findAllByOwnerId(ownerId, PageRequest.of(from, size));
        List<ItemDto> itemDtoList = items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        List<Integer> idItems = itemDtoList.stream()
                .map(ItemDto::getId)
                .collect(Collectors.toList());

        getAllBookingsByItem(itemDtoList, idItems);


        Map<Integer, List<CommentDto>> comments = new HashMap<>();
        List<Comment> commentList = commentRepository.findByItemIdIn(idItems);
        Map<Integer, List<Comment>> commentsMap = new HashMap<>();

        for (Comment comment : commentList) {
            if (!commentsMap.containsKey(comment.getId())) {
                commentsMap.put(comment.getId(), new ArrayList<>());
            }
            commentsMap.get(comment.getId()).add(comment);
        }
        for (Integer id : idItems) {
            List<Comment> itemComments = commentsMap.getOrDefault(id, new ArrayList<>());
            List<CommentDto> commentDtoList = new ArrayList<>();
            for (Comment comment : itemComments) {
                CommentDto commentDto = CommentMapper.toCommentDto(comment);
                commentDtoList.add(commentDto);
            }
            comments.put(id, commentDtoList);
        }

        for (ItemDto itemDto : itemDtoList) {
            itemDto.setComments(comments.get(itemDto.getId()));
        }
        return itemDtoList;
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

    private void setDtoComments(ItemDto dto) {
        List<Comment> comments = itemDao.getAllCommentOneItem(dto.getId());
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentDto.add(CommentMapper.toCommentDto(comment));
        }
        dto.setComments(commentDto);
    }

    public void setDtoNextAndLast(ItemDto dto) {
        Optional<Booking> lastBookingOpt = bookingDao.getLast(dto.getId());
        Optional<Booking> nextBookingOpt = bookingDao.getNext(dto.getId());

        dto.setLastBooking(lastBookingOpt.map(BookingMapper::toInputBookingDto).orElse(null));
        dto.setNextBooking(nextBookingOpt.map(BookingMapper::toInputBookingDto).orElse(null));

    }

    private void getAllBookingsByItem(List<ItemDto> itemDtoList, List<Integer> idItems) {
        // Поиск первого бронирования (nextBooking) для каждого товара
        Map<Integer, InputBookingDto> nextBookings = bookingDao.findFirstByItemIdInAndStartAfterAndStatus(
                        idItems, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(ASC, "start"))
                .stream()
                .map(BookingMapper::toInputBookingDto)
                .collect(Collectors.toMap(InputBookingDto::getItemId, Function.identity()));

        // Установка найденного бронирования как nextBooking для соответствующего товара
        itemDtoList.forEach(itemDto -> itemDto.setNextBooking(nextBookings.get(itemDto.getId())));

        // Поиск последнего бронирования (lastBooking) для каждого товара
        Map<Integer, InputBookingDto> lastBookings = bookingDao.findFirstByItemIdInAndStartLessThanEqualAndStatus(
                        idItems, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(DESC, "start"))
                .stream()
                .map(BookingMapper::toInputBookingDto)
                .collect(Collectors.toMap(InputBookingDto::getItemId, Function.identity()));

        // Установка найденного бронирования как lastBooking для соответствующего товара
        itemDtoList.forEach(itemDto -> itemDto.setLastBooking(lastBookings.get(itemDto.getId())));
    }
}