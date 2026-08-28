package com.banco.api.dto.transaccion;

import com.banco.api.enums.TipoTransaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransaccionResponseDTO(

        Long id,
        TipoTransaccion tipo,
        BigDecimal monto,
        LocalDateTime fecha,
        String descripcion,
        String numeroCuenta
) {
}
