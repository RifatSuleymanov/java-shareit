package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item addItems(Item item); // addItems Добавление новой вещи

    Item updateItem(int itemId, Item item); // updateItems Редактирование вещи

    Item getItemById(int itemId); // getItemsById Просмотр информации о конкретной вещи по ее идентификатору

    List<Item> searchItemByText(String text, int from, int size); // searchItemByText Поиск вещи потенциальным арендатором

    Comment addComment(Comment comment);

    List<Comment> getAllCommentOneItem(int id);

    List<Item> getAllItemsByOneRequest(int requestId);

    List<Item> getAllItemsByMultipleRequests(List<Integer> requestIds);

    Page<Item> findAllByOwnerId(Integer ownerId, Pageable pageable);


}