package com.banco.api.dto.tarjeta;

import com.banco.api.enums.EstadoTarjeta;
import com.banco.api.enums.TipoTarjeta;

import java.time.LocalDate;

public record TarjetaResponseDTO(

        Long id,

        String numeroTarjeta,

        String titular,

        TipoTarjeta tipo,

        EstadoTarjeta estado,

        LocalDate fechaExpiracion

) {
}
