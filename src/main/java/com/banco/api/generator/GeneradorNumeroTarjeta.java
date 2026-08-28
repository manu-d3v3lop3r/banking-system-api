package com.banco.api.generator;

import com.banco.api.repository.TarjetaRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GeneradorNumeroTarjeta {

    private final TarjetaRepository tarjetaRepository;

    public GeneradorNumeroTarjeta(TarjetaRepository tarjetaRepository) {
        this.tarjetaRepository = tarjetaRepository;
    }

    public String generarNumeroTarjeta() {

        String numero;

        do {

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 16; i++) {

                builder.append(ThreadLocalRandom.current().nextInt(10));

            }

            numero = builder.toString();

        } while (tarjetaRepository.existsByNumeroTarjeta(numero));

        return numero;

    }
}
