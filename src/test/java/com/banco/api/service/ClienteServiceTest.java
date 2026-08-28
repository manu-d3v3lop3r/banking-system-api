package com.banco.api.service;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Cliente;
import com.banco.api.exception.ClienteDuplicadoException;
import com.banco.api.exception.EmailDuplicadoException;
import com.banco.api.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deberiaCrearClienteCorrectamente() {

        Cliente cliente = crearCliente();

        when(clienteRepository.existsByDocumento(cliente.getDocumento()))
                .thenReturn(false);

        when(clienteRepository.existsByEmail(cliente.getEmail()))
                .thenReturn(false);

        when(clienteRepository.save(cliente))
                .thenReturn(cliente);

        Cliente resultado = clienteService.crearCliente(cliente);

        assertNotNull(resultado);
        assertEquals(cliente.getNombre(), resultado.getNombre());
        assertEquals(cliente.getApellido(), resultado.getApellido());
        assertEquals(cliente.getDocumento(), resultado.getDocumento());
        assertEquals(cliente.getEmail(), resultado.getEmail());

        verify(clienteRepository).existsByDocumento(cliente.getDocumento());
        verify(clienteRepository).existsByEmail(cliente.getEmail());
        verify(clienteRepository).save(cliente);

        verifyNoMoreInteractions(clienteRepository);

    }

    @Test
    void noDeberiaCrearClienteConDocumentoDuplicado() {

        Cliente cliente = crearCliente();

        when(clienteRepository.existsByDocumento(cliente.getDocumento()))
                .thenReturn(true);

        ClienteDuplicadoException exception = assertThrows(
                ClienteDuplicadoException.class,
                () -> clienteService.crearCliente(cliente)
        );

        assertEquals(
                ApiMessages.CLIENTE_DOCUMENTO_DUPLICADO,
                exception.getMessage()
        );

        verify(clienteRepository).existsByDocumento(cliente.getDocumento());
        verify(clienteRepository, never()).existsByEmail(any());
        verify(clienteRepository, never()).save(any());

        verifyNoMoreInteractions(clienteRepository);

    }

    @Test
    void noDeberiaCrearClienteConEmailDuplicado() {

        Cliente cliente = crearCliente();

        when(clienteRepository.existsByDocumento(cliente.getDocumento()))
                .thenReturn(false);

        when(clienteRepository.existsByEmail(cliente.getEmail()))
                .thenReturn(true);

        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> clienteService.crearCliente(cliente)
        );

        assertEquals(
                ApiMessages.CLIENTE_EMAIL_DUPLICADO,
                exception.getMessage()
        );

        verify(clienteRepository).existsByDocumento(cliente.getDocumento());
        verify(clienteRepository).existsByEmail(cliente.getEmail());
        verify(clienteRepository, never()).save(any());

        verifyNoMoreInteractions(clienteRepository);

    }

    private Cliente crearCliente() {

        Cliente cliente = new Cliente();

        cliente.setNombre("Alfonso");
        cliente.setApellido("Perez");
        cliente.setDocumento("12345678A");
        cliente.setEmail("alfonso@test.com");
        cliente.setTelefono("600123456");

        return cliente;

    }

}