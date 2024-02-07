package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) { // создание пользователя
        log.info("метод addUser");
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) { // объявление пользователя
        log.info("метод updateUser userId " + userId);
        return userService.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) { // Выдача пользователя по id
        log.info("метод getUserById userId " + userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUser() { // выдача всех пользователей
        log.info("метод getAllUser");
        return userService.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) { //удаление пользователя
        log.info("метод deleteUser userId " + userId);
        userService.deleteUser(userId);
    }
}