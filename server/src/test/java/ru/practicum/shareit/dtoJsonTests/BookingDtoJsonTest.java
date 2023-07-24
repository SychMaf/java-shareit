package ru.practicum.shareit.dtoJsonTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    private final UserDto userDto = new UserDto(
            1L,
            "John",
            "user@user.com"
    );
    private final ItemDto itemDto = new ItemDto(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            1L
    );
    private final BookingDto bookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10),
            LocalDateTime.of(2030, 10, 10, 10, 10, 10),
            BookingStatus.APPROVED,
            2L,
            3L,
            userDto,
            itemDto
    );

    @Test
    void bookingDtoTest() throws Exception {
        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start")
                .isEqualTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10).toString());
        assertThat(result).extractingJsonPathValue("$.end")
                .isEqualTo(LocalDateTime.of(2030, 10, 10, 10, 10, 10).toString());
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingDto.getStatus().toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(3);
        assertThat(result).extractingJsonPathValue("$.booker.id", UserDto.class)
                .isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.booker.name", UserDto.class)
                .isEqualTo("John");
        assertThat(result).extractingJsonPathValue("$.booker.email", UserDto.class)
                .isEqualTo("user@user.com");
        assertThat(result).extractingJsonPathValue("$.item.id", ItemDto.class)
                .isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.item.name", ItemDto.class)
                .isEqualTo("Дрель");
        assertThat(result).extractingJsonPathValue("$.item.description", ItemDto.class)
                .isEqualTo("Простая дрель");
        assertThat(result).extractingJsonPathValue("$.item.available", ItemDto.class)
                .isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.item.requestId", ItemDto.class)
                .isEqualTo(1);
    }
}
