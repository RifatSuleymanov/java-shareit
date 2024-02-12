package ru.practicum.shareit.item.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto addItems(ItemDto itemDto, int ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("по вашему id не было найден пользователь"));
        Item item = ItemMapper.toItem(itemDto, user);
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("По вашему id не был найден запрос")));
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto updateItems(int itemId, ItemDto itemDto, int ownerId) {
        Item item = ItemMapper.toItem(itemDto, userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("по вашему id не было найден пользователь")));
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("По вашему id не был найден запрос")));
        }
        Item originalItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вешь"));
        if (!Objects.equals(originalItem.getOwner().getId(), item.getOwner().getId())) {
            throw new NotFoundException("вы не можете редактировать чужие объявления");
        }
        Optional.ofNullable(item.getName()).ifPresent(originalItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(originalItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(originalItem::setAvailable);
        return ItemMapper.toItemDto(originalItem);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemsById(int itemId, int ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вещб"));
        ItemDto dto = ItemMapper.toItemDto(item);
        setDtoComments(dto);
        if (ownerId == item.getOwner().getId()) {
            setDtoNextAndLast(dto);
        }
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getAllItemsOneUser(int ownerId, int from, int size) {
        Page<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId, PageRequest.of(from, size));
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
    @Override
    public List<ItemDto> searchItemByText(String text, int from, int size) {
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        return itemRepository.findAll(PageRequest.of(from, size))
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> containsText(item, text))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }

    @Transactional
    @Override
    public CommentDto addComment(int itemId, int userId, CommentDto commentDto) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndStartIsBefore(userId, itemId, LocalDateTime.now())) {
            throw new BadRequestException("вы не можете оставлять комментарии на вещь которой не пользовались");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вещб"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("по вашему id не было найден пользователь"));
        Comment comment = CommentMapper.toComment(commentDto, user, item);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private void setDtoComments(ItemDto dto) {
        List<Comment> comments = commentRepository.findByItemId(dto.getId());
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentDto.add(CommentMapper.toCommentDto(comment));
        }
        dto.setComments(commentDto);
    }

    private void setDtoNextAndLast(ItemDto dto) {
        Optional<Booking> lastBookingOpt = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                dto.getId(), BookingStatus.APPROVED, LocalDateTime.now());
        Optional<Booking> nextBookingOpt = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                dto.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        dto.setLastBooking(lastBookingOpt.map(BookingMapper::toInputBookingDto).orElse(null));
        dto.setNextBooking(nextBookingOpt.map(BookingMapper::toInputBookingDto).orElse(null));

    }

    private void getAllBookingsByItem(List<ItemDto> itemDtoList, List<Integer> idItems) {
        Map<Integer, InputBookingDto> lastBookings = bookingRepository.findFirstByItemIdInAndStartLessThanEqualAndStatus(
                        idItems, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(DESC, "start"))
                .stream()
                .map(BookingMapper::toInputBookingDto)
                .collect(Collectors.toMap(InputBookingDto::getItemId, Function.identity()));
        itemDtoList.forEach(i -> i.setLastBooking(lastBookings.get(i.getId())));

        Map<Integer, InputBookingDto> nextBookings = bookingRepository.findFirstByItemIdInAndStartAfterAndStatus(
                        idItems, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(ASC, "start"))
                .stream()
                .map(BookingMapper::toInputBookingDto)
                .collect(Collectors.toMap(InputBookingDto::getItemId, Function.identity()));
        itemDtoList.forEach(i -> i.setNextBooking(nextBookings.get(i.getId())));
    }
}