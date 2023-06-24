package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto saveItem(Item item, Long userId);

    ItemDto getItemById(Long itemId, Long useId);

    ItemDto updateItem(Long userId, Long patchId, Map<String, Object> updates);

    List<ItemDto> getAllUserItem(Long userId);

    List<ItemDto> searchItems(Long userId, String text);
}
