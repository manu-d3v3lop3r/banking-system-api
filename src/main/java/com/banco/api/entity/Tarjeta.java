package com.banco.api.entity;

import com.banco.api.enums.EstadoTarjeta;
import com.banco.api.enums.TipoTarjeta;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tarjetas")
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,length = 16)
    private String numeroTarjeta;

    @Column(nullable = false,length = 100)
    private String titular;

    @Column(nullable = false, length = 3)
    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarjeta tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarjeta estado;

    @Column(nullable = false)
    private LocalDate fechaExpiracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    public Tarjeta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public TipoTarjeta getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarjeta tipo) {
        this.tipo = tipo;
    }

    public EstadoTarjeta getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarjeta estado) {
        this.estado = estado;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
