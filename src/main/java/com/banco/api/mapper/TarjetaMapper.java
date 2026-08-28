package com.banco.api.mapper;

import com.banco.api.dto.tarjeta.TarjetaResponseDTO;
import com.banco.api.dto.transaccion.TransaccionResponseDTO;
import com.banco.api.entity.Tarjeta;
import com.banco.api.entity.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarjetaMapper {

    @Mapping(
            target = "numeroTarjeta",
            source = "numeroTarjeta",
            qualifiedByName = "enmascararNumero"
    )
    TarjetaResponseDTO toResponse(Tarjeta tarjeta);

    @Named("enmascararNumero")
    default String enmascararNumero(String numeroTarjeta) {

        if (numeroTarjeta == null || numeroTarjeta.length() < 4) {
            return numeroTarjeta;
        }

        return "**** **** **** "
                + numeroTarjeta.substring(numeroTarjeta.length() - 4);
    }

    List<TransaccionResponseDTO> toResponse(List<Transaccion> transacciones);

}