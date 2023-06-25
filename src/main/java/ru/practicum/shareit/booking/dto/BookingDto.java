package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Date;

@Data
@Builder
public class BookingDto {
    private Long id;
    private Date start;
    private Date end;
    private Long item;
    private Long booker;
    private BookingStatus status;
}
