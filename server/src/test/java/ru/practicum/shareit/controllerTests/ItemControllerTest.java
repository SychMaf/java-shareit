package ru.practicum.shareit.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemDtoLong itemDtoLong;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());

        itemDto = new ItemDto(
                1L,
                "Дрель",
                "Простая дрель",
                true,
                1L
        );

        commentDto = new CommentDto(
                1L,
                "comment",
                "authorName",
                LocalDateTime.of(2023, 10, 10, 10, 10, 10)
        );

        itemDtoLong = new ItemDtoLong(
                1L,
                "Дрель",
                "Простая дрель",
                true,
                1L,
                null,
                null,
                List.of(commentDto)
        );
    }

    @Test
    void addNewItemTest() throws Exception {
        when(itemService.saveItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void getAllUserItemTest() throws Exception {
        when(itemService.getAllUserItem(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoLong));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoLong.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoLong.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoLong.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoLong.getAvailable())))
                .andExpect(jsonPath("$[0].request", is(itemDtoLong.getRequest()), Long.class))
                .andExpect(jsonPath("$[0].comments", hasSize(1)));
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDtoLong);

        mvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoLong.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoLong.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoLong.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoLong.getAvailable())))
                .andExpect(jsonPath("$.request", is(itemDtoLong.getRequest()), Long.class))
                .andExpect(jsonPath("$.comments", hasSize(1)));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void updateItemWhenUserDontHavePermissionTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenThrow(new PermissionException("User with id %d does not have permission for do this"));

        mvc.perform(patch("/items/{itemId}", 3L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItemWhenThrowUnexpectedExceptionTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenThrow(new RuntimeException("smf strange"));

        mvc.perform(patch("/items/{itemId}", 3L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.searchItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "findParam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void writeCommentToItemTest() throws Exception {
        when(itemService.writeCommentToItem(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }
}
