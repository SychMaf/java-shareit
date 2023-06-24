package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemRepository {
    Item saveItem(Item item, Long userId);

    List<Item> getAllUserItem(Long userId);

    Item getItemById(Long itemId);

    Item upgradeItem(Long patchId, Map<String, Object> updates);

    List<Item> searchItem(String text);
}