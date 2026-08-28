package com.banco.api.security;

import com.banco.api.constants.ApiMessages;
import com.banco.api.entity.Usuario;
import com.banco.api.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ApiMessages.USUARIO_NO_AUTENTICADO));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .authorities(
                        usuario.getRoles()
                                .stream()
                                .map(rol -> rol.getNombre().name())
                                .toArray(String[]::new)
                )
                .build();
    }
}
