package com.banco.api.dto.cuenta;

import jakarta.validation.constraints.NotNull;

public record CuentaRequestDTO(

        @NotNull(message = "El identificador del cliente es obligatorio")
        Long clienteId

) {

}
