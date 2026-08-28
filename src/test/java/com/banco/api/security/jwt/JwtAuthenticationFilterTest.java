package com.banco.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.clearContext();

    }

    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();

    }

    @Test
    void deberiaContinuarSinCabeceraAuthorization() throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void deberiaContinuarSiTokenNoEmpiezaPorBearer() throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Basic 123456");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void deberiaContinuarSiTokenEsInvalido() throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        when(jwtService.validarToken("token"))
                .thenReturn(false);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void deberiaAutenticarUsuarioConTokenValido() throws Exception {

        UserDetails user = User.builder()
                .username("admin")
                .password("123")
                .roles("ADMIN")
                .build();

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        when(jwtService.validarToken("token"))
                .thenReturn(true);

        when(jwtService.extraerUsername("token"))
                .thenReturn("admin");

        when(userDetailsService.loadUserByUsername("admin"))
                .thenReturn(user);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        assertEquals(
                "admin",
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );

        verify(filterChain).doFilter(request, response);

    }

}