package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, long userId);

    ItemDto getItemById(Long itemId, long useId);

    ItemDto updateItem(long userId, Long patchId, ItemDto itemDto);

    List<ItemDto> getAllUserItem(long userId);

    List<ItemDto> searchItems(long userId, String text);
}
