package com.banco.api.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {

        JwtProperties properties = new JwtProperties();

        properties.setSecret("MiClaveSuperSecretaParaJwt123456");
        properties.setIssuer("BancoAPI");
        properties.setExpiration(3600000L);

        jwtService = new JwtService(properties);

    }

    @Test
    void deberiaGenerarToken() {

        String token = jwtService.generarToken("admin");

        assertNotNull(token);

        assertFalse(token.isBlank());

    }

    @Test
    void deberiaExtraerUsername() {

        String token = jwtService.generarToken("admin");

        String username = jwtService.extraerUsername(token);

        assertEquals("admin", username);

    }

    @Test
    void deberiaValidarTokenCorrecto() {

        String token = jwtService.generarToken("empleado");

        assertTrue(jwtService.validarToken(token));

    }

    @Test
    void deberiaDetectarTokenInvalido() {

        assertFalse(jwtService.validarToken("token.invalido"));

    }

}