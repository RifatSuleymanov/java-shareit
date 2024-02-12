package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;
    private final String requester = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addRequest(@Valid @RequestBody RequestDto requestDto,
                                             @RequestHeader(requester) Integer requesterId) { //добавить новый запрос вещи.
        log.info("метод addRequest user = " + requester);
        return requestClient.addRequest(requestDto, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestOneUser(@RequestHeader(requester) Integer requesterId) { // получить список своих запросов
        log.info("метод getAllRequestOneUser user = " + requester);
        return requestClient.getAllRequestOneUser(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsAllUsers(
            @RequestHeader(requester) Integer requesterId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "20", required = false) Integer size) { // получить список запросов, созданных другими пользователями.
        log.info("метод getRequestsAllUsers user = " + requester + " from = " + from + " size = " + size);
        return requestClient.getRequestsAllUsers(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Integer requestId,
                                                 @RequestHeader(requester) Integer requesterId) { // получить данные об одном конкретном запросе
        log.info("метод getRequestById user = " + requester + " Id = " + requesterId);
        return requestClient.getRequestById(requestId, requesterId);
    }
}