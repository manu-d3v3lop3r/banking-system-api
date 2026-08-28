package com.banco.api.constants;

public final class ApiMessages {

    private ApiMessages() {
        throw new IllegalStateException("Clase de utilidades");
    }

    // Cliente
    public static final String CLIENTE_NO_ENCONTRADO = "Cliente no encontrado";

    public static final String CLIENTE_DOCUMENTO_DUPLICADO = "Ya existe un cliente con ese documento";

    public static final String CLIENTE_EMAIL_DUPLICADO = "Ya existe un cliente con ese email";

    // Cuenta
    public static final String CUENTA_NO_ENCONTRADA = "Cuenta no encotrada";

    // Tarjeta
    public static final String TARJETA_NO_ENCONTRADA = "Tarjeta no encontrada";

    // Transacciones
    public static final String SALDO_INSUFICIENTE = "Saldo insificiente";

    public static final String MONTO_OBLIGATORIO = "El monto es obligatorio";

    public static final String MONTO_INVALIDO = "El monto debe ser mayor que cero";

    public static final String TRANSFERENCIA_INVALIDA = "No se puede transferir a la misma cuenta";

    public static final String DEPOSITO_REALIZADO = "Depósito realizado";

    public static final String RETIRO_REALIZADO = "Retiro realizado";

    public static final String TRANSFERENCIA_ENVIADA = "Transferencia enviada";

    public static final String TRANSFERENCIA_RECIBIDA = "Transferencia recibida";

    // Seguridad
    public static final String USUARIO_NO_AUTENTICADO = "Usuario no autenticado o credenciales inválidas";

    public static final String ACCESO_DENEGADO = "No tienes permisos para acceder a este recurso";


}