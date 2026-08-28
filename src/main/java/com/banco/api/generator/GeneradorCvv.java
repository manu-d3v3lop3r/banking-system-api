package com.banco.api.generator;

import com.banco.api.repository.TarjetaRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GeneradorCvv {

    private final TarjetaRepository tarjetaRepository;

    public GeneradorCvv(TarjetaRepository tarjetaRepository) {
        this.tarjetaRepository = tarjetaRepository;
    }

    public String generarCvv() {

        return String.format("%03d", ThreadLocalRandom.current().nextInt(1000));

    }
}
