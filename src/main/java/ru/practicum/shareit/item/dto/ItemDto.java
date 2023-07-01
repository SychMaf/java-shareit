package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validator.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;


@Data
@Builder
public class ItemDto {
    @PositiveOrZero(groups = ValidationGroups.Update.class)
    private Long id;
    @NotBlank(groups = ValidationGroups.Create.class)
    private String name;
    @NotBlank(groups = ValidationGroups.Create.class)
    @Size(groups = ValidationGroups.Create.class, max = 500)
    @Size(groups = ValidationGroups.Update.class, max = 500)
    private String description;
    @NotNull(groups = ValidationGroups.Create.class)
    private Boolean available;
    private Long owner;
    private Long request;
}
