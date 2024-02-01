package ru.practicum.shareit.business.user.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.business.exception.NotFoundException;
import ru.practicum.shareit.business.user.repository.UserRepository;
import ru.practicum.shareit.businessInterface.UserDao;
import ru.practicum.shareit.business.user.model.User;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, int id) {
        User originalUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("по вашему id не найден пользователь"));
        Optional.ofNullable(user.getEmail()).ifPresent(originalUser::setEmail);
        Optional.ofNullable(user.getName()).ifPresent(originalUser::setName);
        return userRepository.save(originalUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("по вашему id не было найден пользователь"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}