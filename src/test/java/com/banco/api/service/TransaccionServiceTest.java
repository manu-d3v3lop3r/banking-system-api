package com.banco.api.service;

import com.banco.api.entity.Cuenta;
import com.banco.api.entity.Transaccion;
import com.banco.api.enums.TipoTransaccion;
import com.banco.api.exception.CuentaNoEncontradaException;
import com.banco.api.exception.MontoInvalidoException;
import com.banco.api.exception.SaldoInsuficienteException;
import com.banco.api.exception.TransferenciaInvalidaException;
import com.banco.api.repository.CuentaRepository;
import com.banco.api.repository.TransaccionRepository;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    @Test
    void deberiaDepositarCorrectamente() {

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456789");
        cuenta.setSaldo(BigDecimal.valueOf(100));

        when(cuentaRepository.findByNumeroCuenta("123456789"))
                .thenReturn(Optional.of(cuenta));

        when(transaccionRepository.save(any(Transaccion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaccion resultado = transaccionService.depositar(
                "123456789",
                BigDecimal.valueOf(50)
        );

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(150), cuenta.getSaldo());
        assertEquals(TipoTransaccion.DEPOSITO, resultado.getTipo());

        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void deberiaRetirarCorrectamente() {

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456789");
        cuenta.setSaldo(BigDecimal.valueOf(500));

        when(cuentaRepository.findByNumeroCuenta("123456789"))
                .thenReturn(Optional.of(cuenta));

        when(transaccionRepository.save(any(Transaccion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaccion resultado = transaccionService.retirar(
                "123456789",
                BigDecimal.valueOf(100)
        );

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(400), cuenta.getSaldo());
        assertEquals(TipoTransaccion.RETIRO, resultado.getTipo());

        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void noDeberiaRetirarConSaldoInsuficiente() {

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(BigDecimal.valueOf(50));

        when(cuentaRepository.findByNumeroCuenta("123456789"))
                .thenReturn(Optional.of(cuenta));

        assertThrows(
                SaldoInsuficienteException.class,
                () -> transaccionService.retirar(
                        "123456789",
                        BigDecimal.valueOf(100)
                )
        );

        verify(cuentaRepository, never()).save(any());
        verify(transaccionRepository, never()).save(any());
    }

    @Test
    void deberiaTransferirCorrectamente() {

        Cuenta origen = new Cuenta();
        origen.setNumeroCuenta("111");
        origen.setSaldo(BigDecimal.valueOf(500));

        Cuenta destino = new Cuenta();
        destino.setNumeroCuenta("222");
        destino.setSaldo(BigDecimal.valueOf(100));

        when(cuentaRepository.findByNumeroCuenta("111"))
                .thenReturn(Optional.of(origen));

        when(cuentaRepository.findByNumeroCuenta("222"))
                .thenReturn(Optional.of(destino));

        when(transaccionRepository.save(any(Transaccion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaccion resultado = transaccionService.transferir(
                "111",
                "222",
                BigDecimal.valueOf(200)
        );

        assertEquals(BigDecimal.valueOf(300), origen.getSaldo());
        assertEquals(BigDecimal.valueOf(300), destino.getSaldo());

        assertEquals(TipoTransaccion.TRANSFERENCIA, resultado.getTipo());

        verify(cuentaRepository).save(origen);
        verify(cuentaRepository).save(destino);

        verify(transaccionRepository, times(2))
                .save(any(Transaccion.class));
    }

    @Test
    void noDeberiaTransferirALaMismaCuenta() {

        assertThrows(
                TransferenciaInvalidaException.class,
                () -> transaccionService.transferir(
                        "111",
                        "111",
                        BigDecimal.TEN
                )
        );

        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void noDeberiaAceptarMontoNegativo() {

        assertThrows(
                MontoInvalidoException.class,
                () -> transaccionService.depositar(
                        "123",
                        BigDecimal.valueOf(-50)
                )
        );

        verifyNoInteractions(cuentaRepository);
        verifyNoInteractions(transaccionRepository);
    }

    @Test
    void deberiaRegistrarLaTransaccion() {

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456789");
        cuenta.setSaldo(BigDecimal.ZERO);

        when(cuentaRepository.findByNumeroCuenta("123456789"))
                .thenReturn(Optional.of(cuenta));

        when(transaccionRepository.save(any(Transaccion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transaccionService.depositar(
                "123456789",
                BigDecimal.valueOf(300)
        );

        ArgumentCaptor<Transaccion> captor =
                ArgumentCaptor.forClass(Transaccion.class);

        verify(transaccionRepository).save(captor.capture());

        Transaccion transaccion = captor.getValue();

        assertEquals(TipoTransaccion.DEPOSITO, transaccion.getTipo());
        assertEquals(BigDecimal.valueOf(300), transaccion.getMonto());
        assertEquals("Depósito realizado", transaccion.getDescripcion());
        assertNotNull(transaccion.getFecha());
    }

}
