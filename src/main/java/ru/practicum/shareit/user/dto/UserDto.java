package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.ValidationGroups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = ValidationGroups.Create.class)
    private String name;
    @NotBlank(groups = ValidationGroups.Create.class)
    @Email(groups = ValidationGroups.Create.class)
    @Email(groups = ValidationGroups.Update.class)
    private String email;
}
