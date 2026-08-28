package com.banco.api.mapper;

import com.banco.api.dto.cliente.ClienteSimpleDTO;
import com.banco.api.dto.cuenta.CuentaRequestDTO;
import com.banco.api.dto.cuenta.CuentaResponseDTO;
import com.banco.api.entity.Cliente;
import com.banco.api.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroCuenta", ignore = true)
    @Mapping(target = "saldo", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "transacciones", ignore = true)
    @Mapping(target = "tarjetas", ignore = true)
    Cuenta toEntity(CuentaRequestDTO dto);

    CuentaResponseDTO toResponse(Cuenta cuenta);

    List<CuentaResponseDTO> toResponse(List<Cuenta> cuentas);

    ClienteSimpleDTO toClientesSimpleDTO(Cliente cliente);

}
