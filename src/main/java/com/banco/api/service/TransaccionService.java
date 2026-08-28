package com.banco.api.service;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Cuenta;
import com.banco.api.entity.Transaccion;
import com.banco.api.enums.TipoTransaccion;
import com.banco.api.exception.CuentaNoEncontradaException;
import com.banco.api.exception.MontoInvalidoException;
import com.banco.api.exception.SaldoInsuficienteException;
import com.banco.api.exception.TransferenciaInvalidaException;
import com.banco.api.repository.CuentaRepository;
import com.banco.api.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class TransaccionService {

    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public TransaccionService(CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository) {

        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;

    }

    public Transaccion depositar(String numeroCuenta, BigDecimal monto) {

        validarMonto(monto);

        Cuenta cuenta = obtenerCuenta(numeroCuenta);

        cuenta.setSaldo(cuenta.getSaldo().add(monto));

        cuentaRepository.save(cuenta);

        return registrarTransaccion(cuenta, TipoTransaccion.DEPOSITO, monto, "Depósito realizado");

    }

    public Transaccion retirar(String numeroCuenta, BigDecimal monto) {

        validarMonto(monto);

        Cuenta cuenta = obtenerCuenta(numeroCuenta);

        validarSaldoDisponible(cuenta, monto);

        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));

        cuentaRepository.save(cuenta);

        return registrarTransaccion(cuenta, TipoTransaccion.RETIRO, monto, "Retiro realizado");

    }

    public Transaccion transferir(String cuentaOrigen, String cuentaDestino, BigDecimal monto) {

        if (cuentaOrigen.equals(cuentaDestino)) {

            throw new TransferenciaInvalidaException(ApiMessages.TRANSFERENCIA_INVALIDA);

        }

        validarMonto(monto);

        Cuenta origen = obtenerCuenta(cuentaOrigen);
        Cuenta destino = obtenerCuenta(cuentaDestino);

        validarSaldoDisponible(origen, monto);

        origen.setSaldo(origen.getSaldo().subtract(monto));
        destino.setSaldo(destino.getSaldo().add(monto));

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        registrarTransaccion(origen, TipoTransaccion.TRANSFERENCIA, monto, "Transferencia enviada");

        return registrarTransaccion(destino, TipoTransaccion.TRANSFERENCIA, monto, "Transferencia recibida");

    }

    private Cuenta obtenerCuenta(String numeroCuenta) {

        return cuentaRepository.findByNumeroCuenta(numeroCuenta).orElseThrow(() -> new CuentaNoEncontradaException(ApiMessages.CUENTA_NO_ENCONTRADA));

    }

    private void validarMonto(BigDecimal monto) {

        if (monto == null) {

            throw new MontoInvalidoException(ApiMessages.MONTO_OBLIGATORIO);

        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {

            throw new MontoInvalidoException(ApiMessages.MONTO_INVALIDO);

        }

    }

    private void validarSaldoDisponible(Cuenta cuenta, BigDecimal monto) {

        if (cuenta.getSaldo().compareTo(monto) < 0){

            throw new SaldoInsuficienteException(ApiMessages.SALDO_INSUFICIENTE);

        }

    }

    private Transaccion registrarTransaccion(Cuenta cuenta, TipoTransaccion tipo, BigDecimal monto, String descripcion) {

        Transaccion transaccion = new Transaccion();

        transaccion.setCuenta(cuenta);
        transaccion.setTipo(tipo);
        transaccion.setMonto(monto);
        transaccion.setDescripcion(descripcion);
        transaccion.setFecha(LocalDateTime.now());

        return transaccionRepository.save(transaccion);

    }

}