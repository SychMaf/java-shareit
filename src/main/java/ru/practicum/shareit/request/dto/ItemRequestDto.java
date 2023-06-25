package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requester;
    private Date created;
}