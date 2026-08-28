package com.banco.api.controller;

import com.banco.api.dto.tarjeta.TarjetaRequestDTO;
import com.banco.api.dto.tarjeta.TarjetaResponseDTO;
import com.banco.api.entity.Tarjeta;
import com.banco.api.mapper.TarjetaMapper;
import com.banco.api.service.TarjetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Tarjetas",
        description = "Operaciones realacionadas con ls gestión de tarjetas bancarias"
)
@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final TarjetaMapper tarjetaMapper;

    public TarjetaController(TarjetaService tarjetaService, TarjetaMapper tarjetaMapper) {
        this.tarjetaService = tarjetaService;
        this.tarjetaMapper = tarjetaMapper;
    }

    @Operation(
            summary = "Crear tarjeta",
            description = "Genera una nueva tarjeta asociada a una cuenta bancaria"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarjetaResponseDTO crearTarjeta(@Valid @RequestBody TarjetaRequestDTO request) {

        Tarjeta tarjeta = tarjetaService.crearTarjeta(request.cuentaId(), request.tipo());

        return tarjetaMapper.toResponse(tarjeta);

    }

    @Operation(
            summary = "Obtener tarjeta",
            description = "Obtiene la información de una tarjeta bancaria"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @GetMapping("/{numeroTarjeta}")
    public TarjetaResponseDTO obtenerTarjeta(@PathVariable String numeroTarjeta) {

        Tarjeta tarjeta = tarjetaService.obtenerTarjeta(numeroTarjeta);

        return tarjetaMapper.toResponse(tarjeta);

    }

    @Operation(
            summary = "Bloquear tarjeta",
            description = "Bloquear una tarjeta bancaria"
    )
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    @PatchMapping("/{numeroTarjeta}/bloquear")
    public TarjetaResponseDTO bloquearTarjeta(@PathVariable String numeroTarjeta) {

        Tarjeta tarjeta = tarjetaService.bloquearTarjeta(numeroTarjeta);

        return tarjetaMapper.toResponse(tarjeta);

    }
}