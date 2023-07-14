package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
@Slf4j
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    @Validated
    public ItemRequestDto addNewItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.trace("Got request to add new itemRequest: {}", itemRequestDto);
        return requestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    @Validated
    public List<ItemRequestDto> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.trace("Got request to all user ItemRequest: {}", userId);
        return requestService.getUserResponse(userId);
    }

    @GetMapping("/all")
    @Validated
    public List<ItemRequestDto> getOtherItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("Got request to all ItemRequest: {}", userId);
        return requestService.getAllNotUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @Validated
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long requestId) {
        log.trace("Got request to get itemRequest with id: {}", requestId);
        return requestService.getByRequestId(userId, requestId);
    }
}
