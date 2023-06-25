package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto saveItem(Item item, long userId);

    ItemDto getItemById(Long itemId, long useId);

    ItemDto updateItem(long userId, Long patchId, Map<String, Object> updates);

    List<ItemDto> getAllUserItem(long userId);

    List<ItemDto> searchItems(long userId, String text);
}
