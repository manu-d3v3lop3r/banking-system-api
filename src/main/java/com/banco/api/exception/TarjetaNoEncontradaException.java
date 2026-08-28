package com.banco.api.exception;

import com.banco.api.entity.Tarjeta;

public class TarjetaNoEncontradaException extends RuntimeException {

    public TarjetaNoEncontradaException(String message) {

        super(message);

    }

}
