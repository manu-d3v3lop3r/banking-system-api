package com.banco.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Solicitud de depósito bancario")
public record DepositoRequest(

        @Schema(
                description = "Número de cuenta destino",
                example = "123456789"
        )
        @NotNull(message = "El número de cuenta es obligatoria")
        @Pattern(
                regexp = "\\d{10}",
                message = "El número de cuenta debe contener 10 dígitos"
        )
        String numeroCuenta,

        @Schema(
                description = "Importe a depositar",
                example = "500.00"
        )
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor que cero")
        BigDecimal monto

) {}
