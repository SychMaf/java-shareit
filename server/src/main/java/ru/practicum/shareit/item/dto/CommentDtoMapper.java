package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentDtoMapper {
    public CommentDto toCommentDto(Comment comment, String authorName) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                authorName,
                comment.getCreated()
        );
    }

    public Comment toComment(CommentDto commentDto, User author, Item item, LocalDateTime created) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                author,
                item,
                created
        );
    }
}
