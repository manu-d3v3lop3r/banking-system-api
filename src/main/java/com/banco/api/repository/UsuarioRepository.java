package com.banco.api.repository;

import com.banco.api.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

}
