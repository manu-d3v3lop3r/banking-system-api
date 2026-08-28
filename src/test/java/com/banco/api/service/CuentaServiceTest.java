package com.banco.api.service;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Cliente;
import com.banco.api.entity.Cuenta;
import com.banco.api.enums.EstadoCuenta;
import com.banco.api.exception.ClienteNoEncontradoException;
import com.banco.api.generator.GeneradorNumeroCuenta;
import com.banco.api.repository.ClienteRepository;
import com.banco.api.repository.CuentaRepository;
import jakarta.persistence.GeneratedValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private GeneradorNumeroCuenta generadorNumeroCuenta;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    void deberiaCrearCuentaCorrectamente() {

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        when(generadorNumeroCuenta.generar()).thenReturn("12345678901234567890");

        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cuenta resultado = cuentaService.crearCuenta(1L);

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getSaldo());
        assertEquals(EstadoCuenta.ACTIVA, resultado.getEstado());
        assertEquals(cliente, resultado.getCliente());
        assertEquals("12345678901234567890", resultado.getNumeroCuenta());
        assertNotNull(resultado.getFechaCreacion());

        ArgumentCaptor<Cuenta> captor = ArgumentCaptor.forClass(Cuenta.class);

        verify(cuentaRepository).save(captor.capture());

        Cuenta cuentaCapturada = captor.getValue();

        assertEquals(cliente, cuentaCapturada.getCliente());
        assertEquals(EstadoCuenta.ACTIVA, cuentaCapturada.getEstado());
        assertEquals(BigDecimal.ZERO, cuentaCapturada.getSaldo());

        verify(clienteRepository).findById(1L);
        verify(generadorNumeroCuenta).generar();
        verify(cuentaRepository).save(any(Cuenta.class));

        verifyNoMoreInteractions(clienteRepository, cuentaRepository, generadorNumeroCuenta);

    }

    @Test
    void noDeberiaCrearCuentaSiClienteNoExiste() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        ClienteNoEncontradoException exception = assertThrows(
                ClienteNoEncontradoException.class,
                () -> cuentaService.crearCuenta(1L)
        );

        assertEquals(ApiMessages.CLIENTE_NO_ENCONTRADO, exception.getMessage());

        verify(clienteRepository).findById(1L);
        verify(cuentaRepository, never()).save(any());
        verify(generadorNumeroCuenta, never()).generar();

        verifyNoMoreInteractions(clienteRepository, cuentaRepository, generadorNumeroCuenta);

    }

    @Test
    void deberiaObtenerCuentaPorNumero() {

        Cuenta cuenta = new Cuenta();

        cuenta.setNumeroCuenta("123456789");

        when(cuentaRepository.findByNumeroCuenta("123456789")).thenReturn(Optional.of(cuenta));

        Cuenta resultado = cuentaService.obtenerCuenta("123456789");

        assertNotNull(resultado);
        assertEquals("123456789", resultado.getNumeroCuenta());

        verify(cuentaRepository).findByNumeroCuenta("123456789");
        verifyNoMoreInteractions(cuentaRepository);

    }

    @Test
    void deberiaConsultarSaldoCorrectamente() {

        Cuenta cuenta = new Cuenta();

        cuenta.setSaldo(BigDecimal.valueOf(500));

        when(cuentaRepository.findByNumeroCuenta("123456789")).thenReturn(Optional.of(cuenta));

        BigDecimal saldo = cuentaService.consultarSaldo("123456789");

        assertEquals(BigDecimal.valueOf(500), saldo);

        verify(cuentaRepository).findByNumeroCuenta("123456789");
        verifyNoMoreInteractions(cuentaRepository);

    }

}
