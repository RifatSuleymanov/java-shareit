package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping // addItems Добавление новой вещи
    public ItemDto addItems(@Valid @RequestBody ItemDto itemDto, @RequestHeader(OWNER_ID) int ownerId) {
        return itemService.addItems(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}") // updateItems Редактирование вещи
    public ItemDto updateItems(@PathVariable Integer itemId, @RequestBody ItemDto itemDto, @RequestHeader(OWNER_ID) int ownerId) {
        return itemService.updateItems(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}") // getItemsById Прсмотр информации о конкретной вещи по ее идентификатору
    public ItemDto getItemsById(@PathVariable Integer itemId) {
        return itemService.getItemsById(itemId);
    }

    @GetMapping // getAllItemsOneUser просмотр владельцем списка всех его вещей
    public List<ItemDto> getAllItemsOneUser(@RequestHeader(OWNER_ID) int ownerId) {
        return itemService.getAllItemsOneUser(ownerId);
    }

    @GetMapping("/search") // поиск вещей потенциальным арендатором
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        return itemService.searchItemByText(text);
    }
}