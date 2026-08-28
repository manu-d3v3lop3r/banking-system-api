package com.banco.api.generator;

import com.banco.api.repository.CuentaRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GeneradorNumeroCuenta {

    private final CuentaRepository cuentaRepository;

    public GeneradorNumeroCuenta(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public String generar() {

        String numeroCuenta;

        do {
            numeroCuenta = String.valueOf(
                    ThreadLocalRandom.current()
                            .nextLong(1_000_000_000L, 10_000_000_000L)
            );
        } while (cuentaRepository.existsByNumeroCuenta(numeroCuenta));

        return numeroCuenta;

    }
}
