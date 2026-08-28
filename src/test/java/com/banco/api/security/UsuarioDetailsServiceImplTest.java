package com.banco.api.security;

import com.banco.api.entity.Rol;
import com.banco.api.entity.Usuario;
import com.banco.api.enums.RolNombre;
import com.banco.api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Test
    void deberiaCargarUsuarioCorrectamente() {

        Rol rol = new Rol();
        rol.setNombre(RolNombre.ROLE_ADMIN);

        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword("123456");
        usuario.setActivo(true);
        usuario.setRoles(Set.of(rol));

        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails =
                usuarioDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(usuarioRepository).findByUsername("admin");

    }

    @Test
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {

        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername("admin")
        );

        verify(usuarioRepository).findByUsername("admin");

    }

    @Test
    void deberiaCrearUsuarioDeshabilitado() {

        Rol rol = new Rol();
        rol.setNombre(RolNombre.ROLE_CLIENTE);

        Usuario usuario = new Usuario();
        usuario.setUsername("cliente");
        usuario.setPassword("123456");
        usuario.setActivo(false);
        usuario.setRoles(Set.of(rol));

        when(usuarioRepository.findByUsername("cliente"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails =
                usuarioDetailsService.loadUserByUsername("cliente");

        assertFalse(userDetails.isEnabled());

    }

}