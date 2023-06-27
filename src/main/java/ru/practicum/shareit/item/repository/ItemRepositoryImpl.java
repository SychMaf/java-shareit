package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItems = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Item saveItem(Item item, long userId) {
        item.setId(itemId);
        items.put(itemId, item);
        userItems.computeIfAbsent(userId, k -> new ArrayList<>()).add(item);
        itemId++;
        return item;
    }

    @Override
    public List<Item> getAllUserItem(long userId) {
        return userItems.get(userId);
    }

    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item upgradeItem(Long patchId, Map<String, Object> updates) {
        Item item = items.get(patchId);
        for (String key : updates.keySet()) {
            if (key.equalsIgnoreCase("name")) {
                item.setName(updates.get(key).toString());
            }
            if (key.equalsIgnoreCase("description")) {
                item.setDescription(updates.get(key).toString());
            }
            if (key.equalsIgnoreCase("available")) {
                item.setAvailable((Boolean) updates.get(key));
            }
        }
        items.put(patchId, item);
        return items.get(patchId);
    }

    @Override
    public List<Item> searchItem(String text) {
        return items.values().stream()
                .filter(item -> ((item.getDescription().toLowerCase().contains(text.toLowerCase())
                        || item.getName().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable()))
                .collect(Collectors.toList());
    }
}
