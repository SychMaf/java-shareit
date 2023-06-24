package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@Builder
public class Item {
    @PositiveOrZero
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private Long request;
}
