package com.banco.api.repository;

import com.banco.api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDocumento(String documento);

    Optional<Cliente> findByEmail(String email);

    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);

}
