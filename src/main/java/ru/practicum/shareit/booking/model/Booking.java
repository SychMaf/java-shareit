package ru.practicum.shareit.booking.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Data
public class Booking {
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
