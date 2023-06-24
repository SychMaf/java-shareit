package ru.practicum.shareit.request.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class ItemRequest {
    @PositiveOrZero
    private Long id;
    @Size(max = 500)
    private String description;
    @NotBlank
    private Long requester;
    private Date created;
}
