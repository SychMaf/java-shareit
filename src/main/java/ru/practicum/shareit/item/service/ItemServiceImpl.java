package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.ItemFieldsValidator;
import ru.practicum.shareit.validator.UserFieldsValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        Item item = ItemDtoMapper.toItem(itemDto, userId);
        return ItemDtoMapper.toItemDto(itemRepository.saveItem(item, userId));
    }

    @Override
    public ItemDto getItemById(Long itemId, long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        return ItemDtoMapper.toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public ItemDto updateItem(long userId, Long patchId, ItemDto itemDto) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        ItemFieldsValidator.checkItemToUser(itemRepository, userId, patchId);
        Item item = ItemDtoMapper.toItem(itemDto, userId);
        return ItemDtoMapper.toItemDto(itemRepository.upgradeItem(patchId, item));
    }

    @Override
    public List<ItemDto> getAllUserItem(long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        return itemRepository.getAllUserItem(userId).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(long userId, String text) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchItem(text).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
