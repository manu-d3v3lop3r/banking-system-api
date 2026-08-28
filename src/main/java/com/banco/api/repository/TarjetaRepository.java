package com.banco.api.repository;

import com.banco.api.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

    Optional<Tarjeta> findByNumeroTarjeta(String numeroTarjeta);

    boolean existsByNumeroTarjeta(String numeroTarjeta);

}
