package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.Conflict;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {
    private int nextUserId = 1;
    public Map<Integer, User> userStorage = new HashMap<>(); // хранение юзеров в памяти

    @Override
    public User addUser(User user) {
        checkEmailUserStorage(user);
        user.setId(nextUserId++);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, int id) {
        checkIdUserStorage(id);
        User originalUser = userStorage.get(id);
        if (user.getName() != null) {
            originalUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(originalUser.getEmail())) {
                checkEmailUserStorage(user);
                originalUser.setEmail(user.getEmail());
            }
        }
        return originalUser;
    }

    @Override
    public User getUserByUd(int id) {
        checkIdUserStorage(id);
        return userStorage.get(id);
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public void deleteUser(int id) {
        checkIdUserStorage(id);
        userStorage.remove(id);
    }

    @Override
    public void checkIdUserStorage(int id) {
        if (!userStorage.containsKey(id)) {
            throw new NotFoundException("по вашему id не было найдено пользователя");
        }
    }

    private void checkEmailUserStorage(User user) {
        for (User originalUser : userStorage.values()) {
            if (originalUser.getEmail().equals(user.getEmail())) {
                throw new Conflict("Эта почт уже занята");
            }
        }
    }
}