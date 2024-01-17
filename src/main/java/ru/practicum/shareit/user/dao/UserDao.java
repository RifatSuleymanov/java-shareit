package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    //CRUD операции
    User addUser(User user); // создание пользователя
    User updateUser(User user, int id); // обновление пользователя
    User getUserById(int id); // выдача пользователя по ID
    List<User> getAllUser(); // выдача всех пользователей
    void deleteUser(int id); // удаление пользователя по ID
}