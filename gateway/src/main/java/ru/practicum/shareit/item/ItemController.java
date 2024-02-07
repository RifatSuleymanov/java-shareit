package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private final String user = "X-Sharer-User-Id";

    @PostMapping // addItems Добавление новой вещи
    public ResponseEntity<Object> addItems(@Valid @RequestBody ItemDto itemDto, @RequestHeader(user) Integer ownerId) {
        log.info("метод ddItems . userId " + ownerId);
        return itemClient.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")  //updateItems Редактирование вещи
    public ResponseEntity<Object> updateItems(@PathVariable Integer itemId, @RequestBody ItemDto itemDto,
                                              @RequestHeader(user) Integer ownerId) {
        log.info("метод updateItems . userId " + ownerId + " itemId " + itemId);
        return itemClient.updateItem(itemId, itemDto, ownerId);
    }


    @GetMapping("/{itemId}") // getItemsById Просмотр информации о конкретной вещи по её идентификатору
    public ResponseEntity<Object> getItemsById(@PathVariable Integer itemId, @RequestHeader(user) Integer ownerId) {
        log.info("метод getItemsById . userId " + ownerId + " itemId " + itemId);
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping() // getAllItemsOneUser Просмотр владельцем списка всех его вещей
    public ResponseEntity<Object> getAllItemsOneUser(@RequestHeader(user) Integer ownerId,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                     @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("метод getAllItemsOneUser . userId " + ownerId);
        return itemClient.getAllItemsOneUser(ownerId, from, size);
    }

    @GetMapping("/search") // Поиск вещи потенциальным арендатором
    public ResponseEntity<Object> searchItemByText(@RequestParam String text,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                   @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("метод searchItemByText");
        return itemClient.searchItemByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Integer itemId, @RequestHeader(user) Integer userId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("метод addComment . userId " + userId + " itemId " + itemId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}