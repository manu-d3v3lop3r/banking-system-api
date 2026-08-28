package com.banco.api.dto.tarjeta;

import com.banco.api.enums.TipoTarjeta;
import jakarta.validation.constraints.NotNull;

public record TarjetaRequestDTO(

        @NotNull(message = "La cuenta es obligatoria")
        Long cuentaId,

        @NotNull(message = "El tipo de tarjeta es obligatorio")
        TipoTarjeta tipo

) {
}
