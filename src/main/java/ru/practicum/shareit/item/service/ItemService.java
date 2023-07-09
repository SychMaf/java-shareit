package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, long userId);

    ItemDtoLong getItemById(Long itemId, Long useId);

    ItemDto updateItem(long userId, Long patchId, ItemDto itemDto);

    List<ItemDtoLong> getAllUserItem(long userId, Integer from, Integer size);

    List<ItemDto> searchItems(long userId, String text, Integer from, Integer size);

    CommentDto writeCommentToItem(long userId, Long itemId, CommentDto commentDto);
}
