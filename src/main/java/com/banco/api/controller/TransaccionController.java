package com.banco.api.controller;

import com.banco.api.dto.DepositoRequest;
import com.banco.api.dto.RetiroRequest;
import com.banco.api.dto.TransferenciaRequest;
import com.banco.api.dto.transaccion.TransaccionResponseDTO;
import com.banco.api.entity.Transaccion;
import com.banco.api.mapper.TransaccionMapper;
import com.banco.api.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Transacciones",
        description = "Operaciones bancarias sobre cuentas"
)
@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final TransaccionMapper transaccionMapper;

    public TransaccionController(TransaccionService transaccionService, TransaccionMapper transaccionMapper) {

        this.transaccionService = transaccionService;
        this.transaccionMapper = transaccionMapper;
    }

    @Operation(
            summary = "Realizar depósito",
            description = "Realiza un despósito sobre una cuenta bancaria"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping("/deposito")
    @ResponseStatus(HttpStatus.OK)
    public TransaccionResponseDTO depositar(@Valid @RequestBody DepositoRequest request) {

        Transaccion transaccion = transaccionService.depositar(request.numeroCuenta(), request.monto());

        return transaccionMapper.toResponse(transaccion);

    }

    @Operation(
            summary = "Realizar retiro",
            description = "Retira dinero de una cuenta bancaria"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping("/retiro")
    @ResponseStatus(HttpStatus.OK)
    public TransaccionResponseDTO retirar(@Valid @RequestBody RetiroRequest request){

        Transaccion transaccion = transaccionService.retirar(request.numeroCuenta(), request.monto());

        return transaccionMapper.toResponse(transaccion);

    }

    @Operation(
            summary = "Realizar transferencia",
            description = "Transfiere dinero entre dos cuentas bancarias"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping("/transferencia")
    @ResponseStatus(HttpStatus.OK)
    public TransaccionResponseDTO transferir(@Valid @RequestBody TransferenciaRequest request) {

        Transaccion transaccion = transaccionService.transferir(request.cuentaOrigen(), request.cuentaDestino(), request.monto());

        return transaccionMapper.toResponse(transaccion);

    }

}