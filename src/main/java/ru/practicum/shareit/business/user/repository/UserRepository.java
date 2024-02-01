package ru.practicum.shareit.business.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.business.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}