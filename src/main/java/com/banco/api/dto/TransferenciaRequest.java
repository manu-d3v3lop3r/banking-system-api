package com.banco.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferenciaRequest(

        @Schema(
                description = "Número de cuenta origen",
                example = "123456789"
        )
        @NotBlank(message = "La cuenta origen es obligatoria")
        @Pattern(
                regexp = "\\d{10}",
                message = "El número de cuenta debe contener 10 dígitos"
        )
        String cuentaOrigen,

        @Schema(
                description = "Número de cuenta destino",
                example = "123456789"
        )
        @NotBlank(message = "La cuenta destino es obligatorio")
        @Pattern(
                regexp = "\\d{10}",
                message = "El número de cuenta debe contener 10 dígitos"
        )
        String cuentaDestino,

        @Schema(
                description = "Importe a transferir",
                example = "100.00"
        )
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor que cero")
        BigDecimal monto

) {
}
