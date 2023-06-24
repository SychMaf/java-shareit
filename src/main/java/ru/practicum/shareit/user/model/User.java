package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class User {
    @PositiveOrZero
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
