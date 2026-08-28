package com.banco.api.dto.cuenta;

import com.banco.api.dto.cliente.ClienteSimpleDTO;
import com.banco.api.enums.EstadoCuenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuentaResponseDTO(

        Long id,
        String numeroCuenta,
        BigDecimal saldo,
        EstadoCuenta estado,
        LocalDateTime fechaCreacion,
        ClienteSimpleDTO cliente

) {
}
