package com.banco.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 50, message = "El nombre de usuario no puede superar los 50 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatorio")
        @Size(max = 100, message = "La contraseña no puede superar los 100 caracteres")
        String password
) {

}
