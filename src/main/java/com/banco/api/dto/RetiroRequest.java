package com.banco.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(
        description = "Solicitud de retiro bancario"
)
public record RetiroRequest(

        @Schema(
                description = "Número de cuenta destino",
                example = "123456789"
        )
        @NotBlank(message = "El número de cuenta es obligatorio")
        String numeroCuenta,

        @Schema(
                description = "Importe a retirar",
                example = "100.00"
        )
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor que cero")
        BigDecimal monto

) {
}
