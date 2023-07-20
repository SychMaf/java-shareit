package ru.practicum.shareit.dtoJsonTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            1L
    );

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "description",
            1L,
            LocalDateTime.of(2025, 10, 10, 10, 10, 10),
            List.of(itemDto)
    );

    @Test
    void requestDtoTest() throws Exception {
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.requester")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.created")
                .isEqualTo(LocalDateTime.of(2025, 10, 10, 10, 10, 10).toString());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.items[0].name", ItemDto.class)
                .isEqualTo("Дрель");
        assertThat(result).extractingJsonPathValue("$.items[0].description", ItemDto.class)
                .isEqualTo("Простая дрель");
        assertThat(result).extractingJsonPathValue("$.items[0].available", ItemDto.class)
                .isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.items[0].requestId", ItemDto.class)
                .isEqualTo(1);
    }
}
