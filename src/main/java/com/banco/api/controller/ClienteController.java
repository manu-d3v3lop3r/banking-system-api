package com.banco.api.controller;

import com.banco.api.dto.cliente.ClienteRequestDTO;
import com.banco.api.dto.cliente.ClienteResponseDTO;
import com.banco.api.entity.Cliente;
import com.banco.api.mapper.ClienteMapper;
import com.banco.api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Cliente",
        description = "Operaciones relacionadas con la gestión de clientes bancarios"
)
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {

        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @Operation(
            summary = "Crear cliente",
            description = "Registra un nuevo cliente en el sistema bancario"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO crearCliente(@Valid @RequestBody ClienteRequestDTO request) {

        Cliente cliente = clienteMapper.toEntity(request);

        Cliente clienteGuardado = clienteService.crearCliente(cliente);

        return clienteMapper.toResponse(clienteGuardado);

    }

    @Operation(
            summary = "Obtener cliente",
            description = "Obtiene un cliente por su identificador"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerCliente(@PathVariable Long id){

        Cliente cliente = clienteService.obtenerClientePorId(id);

        return clienteMapper.toResponse(cliente);

    }

    @Operation(
            summary = "Listar clientes",
            description = "Obtiene todos los clientes registrados"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    @GetMapping
    public List<ClienteResponseDTO> listarClientes() {

        return clienteMapper.toResponse(clienteService.listarClientes());

    }
}