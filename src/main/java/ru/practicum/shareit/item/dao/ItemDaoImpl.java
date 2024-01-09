package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemDaoImpl implements ItemDao {
    private int nextItemId = 1;
    public Map<Integer, Item> itemStorage = new HashMap<>(); // хранение вещей в памяти


    @Override
    public Item addItems(Item item) {
        item.setId(nextItemId++);
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItems(int itemId, Item item) {
        checkIdItemStorage(itemId);
        Item originalItem = itemStorage.get(itemId);
        if (!originalItem.getOwner().equals(item.getOwner())) {
            throw new NotFoundException("Вы не можете редактировать чужие объявления");
        }
        if (item.getName() != null) {
            originalItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            originalItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            originalItem.setAvailable(item.getAvailable());
        }
        return originalItem;
    }

    @Override
    public Item getItemsById(int itemId) {
        checkIdItemStorage(itemId);
        return itemStorage.get(itemId);
    }

    @Override
    public List<Item> getAllItemsOneUser(int ownerId) {
        return itemStorage.values()
                .stream()
                .filter(item -> item.getOwner() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemByText(String text) {
        return itemStorage.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> containsText(item, text))
                .collect(Collectors.toList());
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }

    private void checkIdItemStorage(int id) {
        if (!itemStorage.containsKey(id)) {
            throw new NotFoundException("По вашему id не была найдена вещь");
        }
    }
}