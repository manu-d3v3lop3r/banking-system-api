package com.banco.api.service;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Cuenta;
import com.banco.api.entity.Tarjeta;
import com.banco.api.enums.EstadoTarjeta;
import com.banco.api.enums.TipoTarjeta;
import com.banco.api.exception.CuentaNoEncontradaException;
import com.banco.api.exception.TarjetaNoEncontradaException;
import com.banco.api.repository.CuentaRepository;
import com.banco.api.repository.TarjetaRepository;
import com.banco.api.generator.GeneradorCvv;
import com.banco.api.generator.GeneradorNumeroTarjeta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class TarjetaService {

    private final TarjetaRepository tarjetaRepository;
    private final CuentaRepository cuentaRepository;
    private final GeneradorNumeroTarjeta generadorNumeroTarjeta;
    private final GeneradorCvv generadorCvv;

    public TarjetaService(TarjetaRepository tarjetaRepository, CuentaRepository cuentaRepository, GeneradorNumeroTarjeta generarNumeroTarjeta, GeneradorCvv generadorCvv) {
        this.tarjetaRepository = tarjetaRepository;
        this.cuentaRepository = cuentaRepository;
        this.generadorNumeroTarjeta = generarNumeroTarjeta;
        this.generadorCvv = generadorCvv;
    }

    public Tarjeta crearTarjeta(Long cuentaId, TipoTarjeta tipo) {

        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow(() -> new CuentaNoEncontradaException(ApiMessages.CUENTA_NO_ENCONTRADA));

        Tarjeta tarjeta = construirTarjeta(cuenta, tipo);

        return tarjetaRepository.save(tarjeta);

    }

    @Transactional(readOnly = true)
    public Tarjeta obtenerTarjeta(String numeroTarjeta) {

        return tarjetaRepository.findByNumeroTarjeta(numeroTarjeta).orElseThrow(() -> new TarjetaNoEncontradaException(ApiMessages.TARJETA_NO_ENCONTRADA));

    }

    public Tarjeta bloquearTarjeta(String numeroTarjeta) {

        Tarjeta tarjeta = obtenerTarjeta(numeroTarjeta);

        tarjeta.setEstado(EstadoTarjeta.BLOQUEADA);

        return tarjetaRepository.save(tarjeta);

    }

    private Tarjeta construirTarjeta(Cuenta cuenta, TipoTarjeta tipo) {

        Tarjeta tarjeta = new Tarjeta();

        tarjeta.setCuenta(cuenta);
        tarjeta.setTipo(tipo);
        tarjeta.setEstado(EstadoTarjeta.ACTIVA);
        tarjeta.setNumeroTarjeta(generadorNumeroTarjeta.generarNumeroTarjeta());
        tarjeta.setCvv(generadorCvv.generarCvv());
        tarjeta.setFechaExpiracion(LocalDate.now().plusYears(5));

        String titular = cuenta.getCliente().getNombre() + " " + cuenta.getCliente().getApellido();

        tarjeta.setTitular(titular.toUpperCase());

        return tarjeta;

    }

}
