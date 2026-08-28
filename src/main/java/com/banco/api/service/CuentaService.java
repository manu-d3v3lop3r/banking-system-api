package com.banco.api.service;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Cliente;
import com.banco.api.entity.Cuenta;
import com.banco.api.enums.EstadoCuenta;
import com.banco.api.exception.ClienteNoEncontradoException;
import com.banco.api.exception.CuentaNoEncontradaException;
import com.banco.api.repository.ClienteRepository;
import com.banco.api.repository.CuentaRepository;
import com.banco.api.generator.GeneradorNumeroCuenta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final GeneradorNumeroCuenta generarNumeroCuenta;

    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository, GeneradorNumeroCuenta generarNumeroCuenta){

        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.generarNumeroCuenta = generarNumeroCuenta;
    }

    public Cuenta crearCuenta(Long clienteId){

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNoEncontradoException(ApiMessages.CLIENTE_NO_ENCONTRADO));

        Cuenta cuenta = construirCuenta(cliente);

        return cuentaRepository.save(cuenta);

    }

    @Transactional(readOnly = true)
    public Cuenta obtenerCuenta(String numeroCuenta){

        return cuentaRepository.findByNumeroCuenta(numeroCuenta).orElseThrow(() -> new CuentaNoEncontradaException(ApiMessages.CLIENTE_NO_ENCONTRADO));

    }

    @Transactional(readOnly = true)
    public BigDecimal consultarSaldo(String numeroCuenta){

        Cuenta cuenta = obtenerCuenta(numeroCuenta);

        return cuenta.getSaldo();

    }

    private Cuenta construirCuenta(Cliente cliente) {

        Cuenta cuenta = new Cuenta();

        cuenta.setCliente(cliente);
        cuenta.setNumeroCuenta(generarNumeroCuenta.generar());
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setEstado(EstadoCuenta.ACTIVA);
        cuenta.setFechaCreacion(LocalDateTime.now());

        return cuenta;

    }

}
