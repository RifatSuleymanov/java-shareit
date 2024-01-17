package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item addItems(Item item); // addItems Добавление новой вещи

    Item updateItems(int itemId, Item item); // updateItems Редактирование вещи

    Item getItemsById(int itemId); // getItemsById Просмотр информации о конкретной вещи по ее идентификатору

    List<Item> getAllItemsOneUser(int ownerId); // getAllItemsOneUser Просмотр владельцем списка всех его вещей

    List<Item> searchItemByText(String text); // searchItemByText Поиск вещи потенциальным арендатором
}