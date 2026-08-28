package com.banco.api.repository;

import com.banco.api.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByCuentaNumeroCuenta(String numeroCuenta);

}
