package com.banco.api.exception;

import com.banco.api.constants.ApiMessages;
import com.banco.api.dto.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ClienteNoEncontradoException.class,
            CuentaNoEncontradaException.class,
            TarjetaNoEncontradaException.class
    })
    public ResponseEntity<ApiErrorResponse> manejarNoEncontrados(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return construirRespuesta(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );

    }

    @ExceptionHandler({
            ClienteDuplicadoException.class,
            EmailDuplicadoException.class
    })
    public ResponseEntity<ApiErrorResponse> manejarConflictos(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return construirRespuesta(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );

    }

    @ExceptionHandler({
            SaldoInsuficienteException.class,
            MontoInvalidoException.class,
            TransferenciaInvalidaException.class
    })
    public ResponseEntity<ApiErrorResponse> manejarReglasNegocio(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> manejarValidaciones(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Datos de entrada bi válidos");

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                mensaje,
                request
        );

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> manejarAccesoDenegado(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {

        return construirRespuesta(
                HttpStatus.FORBIDDEN,
                ApiMessages.ACCESO_DENEGADO,
                request
        );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> manejarGeneral(
            Exception ex,
            HttpServletRequest request
    ) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request
        );

    }

    private ResponseEntity<ApiErrorResponse> construirRespuesta(
            HttpStatus status,
            String mensaje,
            HttpServletRequest request
    ) {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);

    }
}
