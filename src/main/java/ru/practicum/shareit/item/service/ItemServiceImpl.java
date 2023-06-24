package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.validator.ItemFieldsValidator;
import ru.practicum.shareit.validator.UserFieldsValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserFieldsValidator fieldsValidator;
    private final ItemFieldsValidator itemFieldsValidator;

    @Override
    public ItemDto saveItem(Item item, Long userId) {
        fieldsValidator.checkUserDoesntExist(userId);
        return ItemDto.toItemDto(itemRepository.saveItem(item, userId));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        fieldsValidator.checkUserDoesntExist(userId);
        return ItemDto.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public ItemDto updateItem(Long userId, Long patchId, Map<String, Object> updates) {
        fieldsValidator.checkUserDoesntExist(userId);
        itemFieldsValidator.checkItemToUser(userId, patchId);
        return ItemDto.toItemDto(itemRepository.upgradeItem(patchId, updates));
    }

    @Override
    public List<ItemDto> getAllUserItem(Long userId) {
        fieldsValidator.checkUserDoesntExist(userId);
        return itemRepository.getAllUserItem(userId).stream()
                .map(ItemDto::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(Long userId, String text) {
        fieldsValidator.checkUserDoesntExist(userId);
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchItem(text).stream()
                .map(ItemDto::toItemDto)
                .collect(Collectors.toList());
    }
}
