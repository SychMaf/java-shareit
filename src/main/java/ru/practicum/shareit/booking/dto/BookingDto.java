package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Data
@Builder
public class BookingDto {
    @PositiveOrZero
    private Long id;
    private Date start;
    private Date end;
    @NotBlank
    private Long item;
    @NotBlank
    private Long booker;
    @NotBlank
    private BookingStatus status;
}
