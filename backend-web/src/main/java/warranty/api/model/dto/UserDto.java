package warranty.api.model.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Email;

public record UserDto (
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {}
