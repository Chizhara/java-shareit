package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@Builder
public class UserRequestDto {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
