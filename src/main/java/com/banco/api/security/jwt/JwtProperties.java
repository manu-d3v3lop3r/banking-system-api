package com.banco.api.security.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "La propiedad jwt.secret es obligatoria")
    private String secret;

    @NotBlank(message = "La propiedad jwt.issuer es obligatoria")
    private String issuer;

    @Min(value = 1, message = "La expiración del JWT debe ser mayor de cero")
    private Long expiration;

    public JwtProperties() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
