package com.banco.api.service;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Cliente;
import com.banco.api.exception.ClienteDuplicadoException;
import com.banco.api.exception.ClienteNoEncontradoException;
import com.banco.api.exception.EmailDuplicadoException;
import com.banco.api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {

        this.clienteRepository = clienteRepository;

    }

    public Cliente crearCliente(Cliente cliente) {

        validarClienteDuplicado(cliente);

        return clienteRepository.save(cliente);

    }

    @Transactional(readOnly = true)
    public Cliente obtenerClientePorId(Long id){

        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNoEncontradoException(ApiMessages.CLIENTE_NO_ENCONTRADO));

    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {

        return clienteRepository.findAll();

    }

    private void validarClienteDuplicado(Cliente cliente) {

        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {

            throw new ClienteDuplicadoException(ApiMessages.CLIENTE_DOCUMENTO_DUPLICADO);

        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {

            throw new EmailDuplicadoException(ApiMessages.CLIENTE_EMAIL_DUPLICADO);

        }

    }
}
