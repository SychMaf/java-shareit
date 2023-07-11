package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.inDB.ItemDBRepository;
import ru.practicum.shareit.request.repository.ItemRequestDBRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;
import ru.practicum.shareit.validator.UserFieldsValidator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ItemRequestDBRepository requestRepository;
    private final ItemDBRepository itemRepository;
    private final UserDBRepository userRepository;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestDto, user);
        return ItemRequestDtoMapper.toItemRequestDto(requestRepository.save(itemRequest), List.of());
    }

    @Override
    public List<ItemRequestDto> getUserResponse(Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        List<ItemRequest> itemRequests = requestRepository.findAllByRequester(requester);
        return itemRequests.stream()
                .map(itemRequest -> ItemRequestDtoMapper.toItemRequestDto(itemRequest, itemRepository.findAllByRequest(itemRequest.getId())))
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllNotUser(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequesterNot(user, pageable);
        return itemRequests.stream()
                .map(itemRequest -> ItemRequestDtoMapper.toItemRequestDto(itemRequest, itemRepository.findAllByRequest(itemRequest.getId())))
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getByRequestId(Long userId, Long requestId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id %d does not exist"));
        return ItemRequestDtoMapper.toItemRequestDto(itemRequest, itemRepository.findAllByRequest(itemRequest.getId()));
    }
}
