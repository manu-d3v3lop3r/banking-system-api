package com.banco.api.service;

import com.banco.api.entity.Cliente;
import com.banco.api.entity.Cuenta;
import com.banco.api.entity.Tarjeta;
import com.banco.api.enums.EstadoTarjeta;
import com.banco.api.enums.TipoTarjeta;
import com.banco.api.generator.GeneradorCvv;
import com.banco.api.generator.GeneradorNumeroTarjeta;
import com.banco.api.repository.CuentaRepository;
import com.banco.api.repository.TarjetaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarjetaServiceTest {

    @Mock
    private TarjetaRepository tarjetaRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private GeneradorNumeroTarjeta generadorNumeroTarjeta;

    @Mock
    private GeneradorCvv generadorCvv;

    @InjectMocks
    private TarjetaService tarjetaService;

    @Test
    void deberiaCrearTarjetaCorrectamente() {

        Cliente cliente = new Cliente();
        cliente.setNombre("Alfonso");
        cliente.setApellido("Perez");

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setCliente(cliente);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        when(generadorNumeroTarjeta.generarNumeroTarjeta()).thenReturn("1234567812345678");

        when(generadorCvv.generarCvv()).thenReturn("123");

        Tarjeta tarjetaGuardada = new Tarjeta();

        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjetaGuardada);

        Tarjeta resultado = tarjetaService.crearTarjeta(1L, TipoTarjeta.DEBITO);

        assertNotNull(resultado);

        ArgumentCaptor<Tarjeta> captor = ArgumentCaptor.forClass(Tarjeta.class);

        verify(tarjetaRepository).save(captor.capture());

        Tarjeta tarjeta = captor.getValue();

        assertEquals(cuenta, tarjeta.getCuenta());
        assertEquals(TipoTarjeta.DEBITO, tarjeta.getTipo());
        assertEquals(EstadoTarjeta.ACTIVA, tarjeta.getEstado());
        assertEquals("1234567812345678", tarjeta.getNumeroTarjeta());
        assertEquals("123", tarjeta.getCvv());
        assertEquals("ALFONSO PEREZ", tarjeta.getTitular());
        assertEquals(LocalDate.now().plusYears(5), tarjeta.getFechaExpiracion());

    }

    @Test
    void deberiaObtenerTarjetaPorNumero() {

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta("1234567812345678");

        when(tarjetaRepository.findByNumeroTarjeta("1234567812345678")).thenReturn(Optional.of(tarjeta));

        Tarjeta resultado = tarjetaService.obtenerTarjeta("1234567812345678");

        assertNotNull(resultado);
        assertEquals("1234567812345678", resultado.getNumeroTarjeta());

    }

    @Test
    void deberiaBloquearTarjeta() {

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setEstado(EstadoTarjeta.ACTIVA);

        when(tarjetaRepository.findByNumeroTarjeta("1234567812345678")).thenReturn(Optional.of(tarjeta));

        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);

        Tarjeta resultado = tarjetaService.bloquearTarjeta("1234567812345678");

        assertEquals(EstadoTarjeta.BLOQUEADA, resultado.getEstado());

        verify(tarjetaRepository).save(tarjeta);

    }
}
