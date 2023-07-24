package ru.practicum.shareit.UnitTests;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDBRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestDBRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDBRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class RequestLogicTest {
    @Mock
    UserDBRepository userRepository;
    @Mock
    ItemDBRepository itemRepository;
    @Mock
    ItemRequestDBRepository requestRepository;
    @InjectMocks
    RequestServiceImpl requestService;
    private final ItemDto itemDto = new ItemDto(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            null
    );
    private final User user = new User(
            1L,
            "John",
            "first@user.com"
    );
    private final Item item = new Item(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            user,
            null
    );
    private final ItemRequest itemRequest = new ItemRequest(
            1L,
            "description",
            user,
            LocalDateTime.of(2025, 10, 10, 10, 10, 10)
    );
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "description",
            1L,
            LocalDateTime.of(2025, 10, 10, 10, 10, 10),
            List.of(itemDto)
    );

    @Test
    void createItemRequestTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestDto getItemRequestDto = requestService.createItemRequest(itemRequestDto, 1L);
        assertEquals(itemRequestDto.getId(), getItemRequestDto.getId());
        assertEquals(itemRequestDto.getDescription(), getItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getRequester(), getItemRequestDto.getRequester());
    }

    @Test
    void getUserResponseTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findAllByRequest(anyLong()))
                .thenReturn(List.of(item));
        Mockito
                .when(requestRepository.findAllByRequester(any(User.class)))
                .thenReturn(List.of(itemRequest));
        List<ItemRequestDto> getItemRequestDto = requestService.getUserResponse(1L);
        assertThat(getItemRequestDto).hasSize(1).contains(itemRequestDto);
    }

    @Test
    void getAllNotUserTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findAllByRequest(anyLong()))
                .thenReturn(List.of(item));
        Mockito
                .when(requestRepository.findAllByRequesterNot(any(User.class), any(Pageable.class)))
                .thenReturn(List.of(itemRequest));
        List<ItemRequestDto> getItemRequestDto = requestService.getAllNotUser(1L, 1, 1);
        assertThat(getItemRequestDto).hasSize(1).contains(itemRequestDto);
    }

    @Test
    void getByRequestIdTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(itemRepository.findAllByRequest(anyLong()))
                .thenReturn(List.of(item));
        ItemRequestDto getItemRequestDto = requestService.getByRequestId(1L, 1L);
        assertEquals(itemRequestDto.getId(), getItemRequestDto.getId());
        assertEquals(itemRequestDto.getDescription(), getItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getRequester(), getItemRequestDto.getRequester());
        assertEquals(itemRequestDto.getItems(), getItemRequestDto.getItems());
    }
}
