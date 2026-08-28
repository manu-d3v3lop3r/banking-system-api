package com.banco.api.controller;

import com.banco.api.dto.cuenta.CuentaRequestDTO;
import com.banco.api.dto.cuenta.CuentaResponseDTO;
import com.banco.api.entity.Cuenta;
import com.banco.api.mapper.CuentaMapper;
import com.banco.api.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Cuentas",
        description = "Operaciones relacionadas con la gestión de cuentas bancarias"
)
@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final CuentaMapper cuentaMapper;

    public CuentaController(CuentaService cuentaService, CuentaMapper cuentaMapper) {

        this.cuentaService = cuentaService;
        this.cuentaMapper = cuentaMapper;

    }

    @Operation(
            summary = "Crear cuenta bancaria",
            description = "Crear una nueva cuenta bancaria para un cliente existente"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponseDTO crearCuenta(@Valid @RequestBody CuentaRequestDTO request) {

        Cuenta cuenta = cuentaService.crearCuenta(request.clienteId());

        return cuentaMapper.toResponse(cuenta);

    }

    @Operation(
            summary = "Consultar cuenta",
            description = "Obtiene la información de una cuenta bancaria mediante su número"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{numeroCuenta}")
    public CuentaResponseDTO obtenerCuenta(@PathVariable String numeroCuenta) {

        Cuenta cuenta = cuentaService.obtenerCuenta(numeroCuenta);

        return cuentaMapper.toResponse(cuenta);

    }

    @Operation(
            summary = "Consultar saldo",
            description = "Devuelve el saldo actual de una cuenta bancaria"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO','CLIENTE')")
    @GetMapping("/{numeroCuenta}/saldo")
    public BigDecimal consultarSaldo(@PathVariable String numeroCuenta) {

        return cuentaService.consultarSaldo(numeroCuenta);

    }

}