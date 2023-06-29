package ru.practicum.shareit.request.model;

import lombok.Data;

import java.util.Date;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private Long requester;
    private Date created;
}
