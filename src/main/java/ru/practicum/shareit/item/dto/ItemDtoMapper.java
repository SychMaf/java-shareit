package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemDtoMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()
        );
    }

    public ItemDtoLong toItemDtoLong(Item item, List<Comment> commentList) {
        return new ItemDtoLong(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null,
                null,
                null,
                commentList.stream()
                        .map(comment -> CommentDtoMapper.toCommentDto(comment, comment.getAuthor().getName()))
                        .collect(Collectors.toList())
        );
    }

    public Item toItem(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                itemDto.getRequestId()
        );
    }

    public Item updateItem(ItemDto itemDto, Item item) {
        return new Item(
                item.getId(),
                itemDto.getName() != null ? itemDto.getName() : item.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription(),
                itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable(),
                item.getOwner(),
                itemDto.getRequestId()
        );
    }
}
