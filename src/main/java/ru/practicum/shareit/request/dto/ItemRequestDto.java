package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    @PositiveOrZero
    private Long id;
    @Size(max = 500)
    @NotBlank
    private String description;
    private Long requester;
    private LocalDateTime created;
    List<ItemDto> items;
}