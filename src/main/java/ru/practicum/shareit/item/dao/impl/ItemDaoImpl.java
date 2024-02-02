package ru.practicum.shareit.item.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dao.ItemDao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public Item addItems(Item item) {
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(int itemId, Item item) {
        Item originalItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вешь"));
        if (!Objects.equals(originalItem.getOwner().getId(), item.getOwner().getId())) {
            throw new NotFoundException("вы не можете редактировать чужие объявления");
        }
        Optional.ofNullable(item.getName()).ifPresent(originalItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(originalItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(originalItem::setAvailable);
        return itemRepository.save(originalItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вещб"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> searchItemByText(String text, int from, int size) {
        return itemRepository.findAll(PageRequest.of(from, size))
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> containsText(item, text))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        comment.setCreated(LocalDateTime.now()); // максимальная свежая дата перед добавлением в бд
        return commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentOneItem(int id) {
        return commentRepository.findByItemId(id);
    }

    @Override
    public List<Item> getAllItemsByOneRequest(int requestId) {
        return itemRepository.findAllByRequestId(requestId);
    }

    public List<Item> getAllItemsByMultipleRequests(List<Integer> requestIds) {
        List<Item> items = new ArrayList<>();
        for (Integer requestId : requestIds) {
            items.addAll(itemRepository.findAllByRequestId(requestId));
        }
        return items;
    }

    @Override
    public Page<Item> findAllByOwnerId(Integer ownerId, Pageable pageable) {
        return itemRepository.findAllByOwnerId(ownerId, pageable);
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}