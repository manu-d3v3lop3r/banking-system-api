package com.banco.api.mapper;

import com.banco.api.dto.transaccion.TransaccionResponseDTO;
import com.banco.api.entity.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta")
    TransaccionResponseDTO toResponse(Transaccion transaccion);

}
