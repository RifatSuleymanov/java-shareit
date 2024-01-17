package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final String OWNER_ID = "X-Sharer-User-Id";
    private ItemService itemService;

    @PostMapping // addItems Добавление новой вещи
    public ItemDto addItems(@Valid @RequestBody ItemDto itemDto, @RequestHeader(OWNER_ID) int ownerId) {
        log.info("метод addItems . userId " + ownerId);
        return itemService.addItems(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}") // updateItems Редактирование вещи
    public ItemDto updateItems(@PathVariable Integer itemId, @RequestBody ItemDto itemDto, @RequestHeader(OWNER_ID) int ownerId) {
        log.info("метод updateItems . userId " + ownerId + " itemId " + itemId);
        return itemService.updateItems(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}") // getItemsById Прсмотр информации о конкретной вещи по ее идентификатору
    public ItemDto getItemsById(@PathVariable Integer itemId, @RequestHeader(OWNER_ID) Integer ownerId) {
        log.info("метод getItemsById . userId " + ownerId + " itemId " + itemId);
        return itemService.getItemsById(itemId, ownerId);
    }

    @GetMapping // getAllItemsOneUser просмотр владельцем списка всех его вещей
    public List<ItemDto> getAllItemsOneUser(@RequestHeader(OWNER_ID) int ownerId) {
        log.info("метод getAllItemsOneUser . userId " + ownerId);
        return itemService.getAllItemsOneUser(ownerId);
    }

    @GetMapping("/search") // поиск вещей потенциальным арендатором
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        log.info("метод searchItemByText");
        return itemService.searchItemByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Integer itemId, @RequestHeader(OWNER_ID) Integer userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("метод addComment . userId " + userId + " itemId " + itemId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}