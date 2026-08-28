package com.banco.api.controller;

import com.banco.api.config.TestSecurityConfig;
import com.banco.api.dto.DepositoRequest;
import com.banco.api.dto.RetiroRequest;
import com.banco.api.dto.TransferenciaRequest;
import com.banco.api.dto.transaccion.TransaccionResponseDTO;
import com.banco.api.entity.Transaccion;
import com.banco.api.enums.TipoTransaccion;
import com.banco.api.mapper.TransaccionMapper;
import com.banco.api.security.SecurityConfig;
import com.banco.api.service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransaccionController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
))
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransaccionService transaccionService;

    @MockitoBean
    private TransaccionMapper transaccionMapper;

    @Test
    void deberiaRealizarDeposito() throws Exception {

        DepositoRequest request = new DepositoRequest("1234567890", BigDecimal.valueOf(500));

        Transaccion transaccion = new Transaccion();

        TransaccionResponseDTO response = new TransaccionResponseDTO(
                1L,
                TipoTransaccion.DEPOSITO,
                BigDecimal.valueOf(500),
                LocalDateTime.now(),
                "Depósito realizado",
                "1234567890"
        );

        when(transaccionService.depositar(
                eq("1234567890"),
                eq(BigDecimal.valueOf(500))))
                .thenReturn(transaccion);

        when(transaccionMapper.toResponse(transaccion))
                .thenReturn(response);

        mockMvc.perform(post("/api/transacciones/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("DEPOSITO"))
                .andExpect(jsonPath("$.monto").value(500));

    }

    @Test
    void deberiaRealizarRetiro() throws Exception {

        RetiroRequest request = new RetiroRequest("123456789", BigDecimal.valueOf(100));

        Transaccion transaccion = new Transaccion();

        TransaccionResponseDTO response = new TransaccionResponseDTO(
                1L,
                TipoTransaccion.RETIRO,
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                "Retiro realizado",
                "123456789"
        );

        when(transaccionService.retirar(
                eq("123456789"),
                eq(BigDecimal.valueOf(100))))
                .thenReturn(transaccion);

        when(transaccionMapper.toResponse(transaccion))
                .thenReturn(response);

        mockMvc.perform(post("/api/transacciones/retiro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("RETIRO"))
                .andExpect(jsonPath("$.monto").value(100));

    }

    @Test
    void deberiaRealizarTransferencia() throws Exception {

        TransferenciaRequest request = new TransferenciaRequest("1111111111", "2222222222", BigDecimal.valueOf(250));

        Transaccion transaccion = new Transaccion();

        TransaccionResponseDTO response = new TransaccionResponseDTO(
                1L,
                TipoTransaccion.TRANSFERENCIA,
                BigDecimal.valueOf(250),
                LocalDateTime.now(),
                "Transferencia recibida",
                "2222222222"
        );

        when(transaccionService.transferir(
                eq("1111111111"),
                eq("2222222222"),
                eq(BigDecimal.valueOf(250))))
                .thenReturn(transaccion);

        when(transaccionMapper.toResponse(transaccion))
                .thenReturn(response);

        mockMvc.perform(post("/api/transacciones/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("TRANSFERENCIA"))
                .andExpect(jsonPath("$.monto").value(250));

    }

    @Test
    void noDeberiaDepositarSinNumeroCuenta() throws Exception {

        DepositoRequest request = new DepositoRequest(null, BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/transacciones/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void noDeberiaRetirarConMontoNegativo() throws Exception {

        RetiroRequest request = new RetiroRequest("123456789", BigDecimal.valueOf(-50));

        mockMvc.perform(post("/api/transacciones/retiro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void noDeberiaTransferirSinCuentaDestino() throws Exception {

        TransferenciaRequest request = new TransferenciaRequest("111111111",null,  BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/transacciones/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

}