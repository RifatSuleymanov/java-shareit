package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final String requester = "X-Sharer-User-Id";
    private RequestService requestService;

    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader(requester) Integer requesterId) {    //Добавить новый запрос вещи
        log.info("метод addRequest user = " + requester);
        return requestService.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestOneUser(@RequestHeader(requester) Integer requesterId) { // получить список своих запросов
        log.info("метод getAllRequestOneUser user = " + requester);
        return requestService.getAllRequestOneUser(requesterId);
    }

    //Получить список запросов, созданными другими пользователями
    @GetMapping("/all")
    public List<ItemRequestDto> getRequestsAllUsers(@RequestHeader(requester) Integer requesterId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                    @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("метод getRequestsAllUsers user = " + requester + " from = " + from + " size " + size);
        return requestService.getRequestAllUser(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Integer requestId,
                                         @RequestHeader(requester) Integer requesterId) { // получить данные об одном запросе
        log.info("Метод getRequestById user = " + requesterId + " id = " + requesterId);
        return requestService.getRequestById(requestId, requesterId);
    }
}