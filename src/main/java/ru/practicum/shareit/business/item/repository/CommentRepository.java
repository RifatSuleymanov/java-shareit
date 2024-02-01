package ru.practicum.shareit.business.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.business.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItemId(Integer itemId);

    List<Comment> findAllByItemId(Integer itemId);
}