package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getUserResponse(Long userId);

    List<ItemRequestDto> getAllNotUser(Long userId, Integer from, Integer size);

    ItemRequestDto getByRequestId(Long userId, Long requestId);
}
