package com.banco.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.verifier = JWT.require(algorithm)
                .withIssuer(jwtProperties.getIssuer())
                .build();
    }

    public String generarToken(String username) {

        Date ahora = new Date();

        Date expiracion = new Date(ahora.getTime() + jwtProperties.getExpiration());

        return JWT.create()
                .withSubject(username)
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(ahora)
                .withExpiresAt(expiracion)
                .sign(algorithm);

    }

    public String extraerUsername(String token) {

        return verifier
                .verify(token)
                .getSubject();

    }

    public boolean validarToken(String token) {

        try {

            verifier.verify(token);

            return true;

        } catch (JWTVerificationException ex) {

            return false;

        }

    }
}
