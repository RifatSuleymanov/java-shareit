package ru.practicum.shareit.business.item.mapper;

import ru.practicum.shareit.business.item.dto.CommentDto;
import ru.practicum.shareit.business.item.model.Comment;
import ru.practicum.shareit.business.item.model.Item;
import ru.practicum.shareit.business.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    public static Comment toComment(CommentDto commentDto, User user, Item item) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        return comment;
    }
}