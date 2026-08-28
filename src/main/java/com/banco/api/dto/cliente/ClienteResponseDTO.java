package com.banco.api.dto.cliente;

public record ClienteResponseDTO(

        Long id,
        String nombre,
        String apellido,
        String documento,
        String email
) {
}
