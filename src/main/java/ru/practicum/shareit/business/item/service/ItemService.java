package ru.practicum.shareit.business.item.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.business.item.dto.ItemDto;
import ru.practicum.shareit.businessInterface.BookingDao;
import ru.practicum.shareit.business.booking.mapper.BookingMapper;
import ru.practicum.shareit.business.booking.model.Booking;
import ru.practicum.shareit.business.booking.model.BookingStatus;
import ru.practicum.shareit.business.booking.repository.BookingRepository;
import ru.practicum.shareit.businessInterface.ItemDao;
import ru.practicum.shareit.business.item.dto.CommentDto;
import ru.practicum.shareit.business.item.mapper.CommentMapper;
import ru.practicum.shareit.business.item.mapper.ItemMapper;
import ru.practicum.shareit.business.item.model.Comment;
import ru.practicum.shareit.business.item.model.Item;
import ru.practicum.shareit.business.item.repository.CommentRepository;
import ru.practicum.shareit.business.item.repository.ItemRepository;
import ru.practicum.shareit.businessInterface.RequestDao;
import ru.practicum.shareit.businessInterface.UserDao;
import ru.practicum.shareit.business.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

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
        List<Item> items = itemRepository.findAllByOwnerId(ownerId, PageRequest.of(from, size))
                .stream()
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemDto> itemDtoList = items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        List<ItemDto> fullItemDtoList = new ArrayList<>();

        for (ItemDto itemDto : itemDtoList) {
            List<CommentDto> comments = commentRepository.findAllByItemId(itemDto.getId())
                    .stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList());

            List<Booking> lastBooking = bookingRepository.findTop1BookingByItemIdAndStartIsBeforeAndStatusIs(
                    itemDto.getId(),
                    LocalDateTime.now(),
                    BookingStatus.APPROVED,
                    Sort.by(Sort.Direction.DESC, "start"));


            List<Booking> nextBooking = bookingRepository.findTop1BookingByItemIdAndStartIsAfterAndStatusIs(
                    itemDto.getId(),
                    LocalDateTime.now(),
                    BookingStatus.APPROVED,
                    Sort.by(Sort.Direction.ASC, "start"));

            itemDto.setLastBooking(lastBooking.isEmpty() ? null : BookingMapper.toInputBookingDto(lastBooking.get(0)));


            itemDto.setNextBooking(nextBooking.isEmpty() ? null : BookingMapper.toInputBookingDto(nextBooking.get(0)));

            fullItemDtoList.add(itemDto);
        }

        fullItemDtoList.sort(Comparator.comparing(o -> {
            if (o.getLastBooking() == null) {
                return null;
            } else {
                return o.getLastBooking().getStart();
            }
        }, Comparator.nullsLast(Comparator.reverseOrder())));

        for (ItemDto itemDto : fullItemDtoList) {
            if (itemDto.getLastBooking() != null && itemDto.getLastBooking().getBookerId() == null) {
                itemDto.setLastBooking(null);
            }

            if (itemDto.getNextBooking() != null && itemDto.getNextBooking().getBookerId() == null) {
                itemDto.setNextBooking(null);
            }
        }

        return fullItemDtoList;
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
}