package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    public final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userDao.addUser(user));
    }

    public UserDto updateUser(UserDto userDto, int id) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userDao.updateUser(user, id));
    }

    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userDao.getUserByUd(id));
    }

    public List<UserDto> getAllUser() {
        return userDao.getAllUser().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}