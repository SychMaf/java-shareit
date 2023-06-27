package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
public class UserDto {
    @PositiveOrZero
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
