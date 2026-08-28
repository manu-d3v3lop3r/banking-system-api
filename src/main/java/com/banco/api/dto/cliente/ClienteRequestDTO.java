package com.banco.api.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
        String apellido,

        @NotBlank(message = "El documento es obligatorio")
        @Pattern(
                regexp = "\\d{8}[A-Za-z]",
                message = "Formato de documento no válido"
        )
        String documento,

        @Email(message = "El email no es válido")
        @NotBlank(message = "El email es obligatorio")
        @Size(max = 100, message = "El email no puede superar lso 100 caracteres")
        String email


) {
}
