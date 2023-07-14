package ru.practicum.shareit.dtoJsonTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> jsonItemDto;
    @Autowired
    private JacksonTester<ItemDtoLong> jsonItemDtoLong;
    private final ItemDto itemDto = new ItemDto(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            1L
    );
    private final CommentDto commentDto = new CommentDto(
            1L,
            "comment",
            "authorName",
            LocalDateTime.of(2023, 10, 10, 10, 10, 10)
    );
    private final UserDto userDto = new UserDto(
            1L,
            "John",
            "user@user.com"
    );
    private final BookingDto lastBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10),
            LocalDateTime.of(2030, 10, 10, 10, 10, 10),
            BookingStatus.APPROVED,
            2L,
            3L,
            userDto,
            itemDto
    );
    private final ItemDtoLong itemDtoLong = new ItemDtoLong(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            1L,
            lastBookingDto,
            null,
            List.of(commentDto)
    );

    @Test
    void itemDtoTest() throws Exception {
        JsonContent<ItemDto> result = jsonItemDto.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Простая дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(1);
    }

    @Test
    void itemDtoLongTest() throws Exception {
        JsonContent<ItemDtoLong> result = jsonItemDtoLong.write(itemDtoLong);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Простая дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.request")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.status")
                .isEqualTo(lastBookingDto.getStatus().toString());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text")
                .isEqualTo("comment");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo("authorName");
        assertThat(result).extractingJsonPathValue("$.comments[0].created")
                .isEqualTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10).toString());

    }
}
