package com.banco.api.repository;

import com.banco.api.entity.Rol;
import com.banco.api.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(RolNombre nombre);

}
