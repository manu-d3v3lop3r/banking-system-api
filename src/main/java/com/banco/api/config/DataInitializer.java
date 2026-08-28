package com.banco.api.config;

import com.banco.api.entity.Rol;
import com.banco.api.entity.Usuario;
import com.banco.api.enums.RolNombre;
import com.banco.api.repository.RolRepository;
import com.banco.api.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        crearRoles();
        crearUsuarios();

        LOGGER.info("Datos iniciales cargados correctamente.");

    }

    private void crearRoles() {

        if (rolRepository.count() > 0) {
            return;
        }

        Rol admin = new Rol();
        admin.setNombre(RolNombre.ROLE_ADMIN);
        admin.setDescripcion("Administrador del sistema");

        Rol empleado = new Rol();
        empleado.setNombre(RolNombre.ROLE_EMPLEADO);
        empleado.setDescripcion("Empleado bancario");

        Rol cliente = new Rol();
        cliente.setNombre(RolNombre.ROLE_CLIENTE);
        cliente.setDescripcion("Cliente bancario");

        rolRepository.save(admin);
        rolRepository.save(empleado);
        rolRepository.save(cliente);

    }

    private void crearUsuarios() {

        if (usuarioRepository.count() > 0) {
            return;
        }

        Rol adminRol = rolRepository.findByNombre(RolNombre.ROLE_ADMIN).orElseThrow(() -> new IllegalStateException("No existe el rol ROLE_ADMIN"));

        Rol empleadoRol = rolRepository.findByNombre(RolNombre.ROLE_EMPLEADO).orElseThrow(() -> new IllegalStateException("No existe el rol ROLE_EMPLEADO"));

        Rol clienteRol = rolRepository.findByNombre(RolNombre.ROLE_CLIENTE).orElseThrow(() -> new IllegalStateException("No existe el rol ROLE_CLIENTE"));

        usuarioRepository.save(crearUsuario("admin", "admin123", adminRol));
        usuarioRepository.save(crearUsuario("empleado", "empleado123", empleadoRol));
        usuarioRepository.save(crearUsuario("cliente", "cliente123", clienteRol));

    }

    private Usuario crearUsuario(String username, String password, Rol rol) {

        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setActivo(true);
        usuario.setRoles(Set.of(rol));

        return usuario;

    }

}