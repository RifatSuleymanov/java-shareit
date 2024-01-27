package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
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
    public Item updateItems(int itemId, Item item) {
        Item originalItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вешь"));
        if (originalItem.getOwner().getId() != item.getOwner().getId()) {
            throw new OwnerException("вы не можете редактировать чужие объявления");
        }
        Optional.ofNullable(item.getName()).ifPresent(originalItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(originalItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(originalItem::setAvailable);
        return itemRepository.save(originalItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemsById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("по вашему id не была найдена вещб"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getAllItemsOneUser(int ownerId) {
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Item> searchItemByText(String text) {
        return itemRepository.findAll()
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
    @Transactional
    public List<Comment> getAllCommentOneItem(int id) {
        return commentRepository.findByItemId(id);
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}