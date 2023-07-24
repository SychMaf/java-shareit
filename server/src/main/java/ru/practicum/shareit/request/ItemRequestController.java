package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto addNewItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        log.trace("SERVER: request to add new itemRequest: {}", itemRequestDto);
        return requestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.trace("SERVER: request to all user ItemRequest: {}", userId);
        return requestService.getUserResponse(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.trace("SERVER: request to all ItemRequest: {}", userId);
        return requestService.getAllNotUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long requestId) {
        log.trace("SERVER: request to get itemRequest with id: {}", requestId);
        return requestService.getByRequestId(userId, requestId);
    }
}
