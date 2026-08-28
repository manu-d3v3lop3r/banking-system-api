package com.banco.api.mapper;

import com.banco.api.dto.cliente.ClienteRequestDTO;
import com.banco.api.dto.cliente.ClienteResponseDTO;
import com.banco.api.entity.Cliente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    Cliente toEntity(ClienteRequestDTO dto);

    ClienteResponseDTO toResponse(Cliente entity);

    List<ClienteResponseDTO> toResponse(List<Cliente> entities);

}
