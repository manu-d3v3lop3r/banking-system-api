package com.banco.api.controller;

import com.banco.api.config.TestSecurityConfig;
import com.banco.api.dto.cliente.ClienteSimpleDTO;
import com.banco.api.dto.cuenta.CuentaRequestDTO;
import com.banco.api.dto.cuenta.CuentaResponseDTO;
import com.banco.api.entity.Cuenta;
import com.banco.api.enums.EstadoCuenta;
import com.banco.api.mapper.CuentaMapper;
import com.banco.api.security.SecurityConfig;
import com.banco.api.service.CuentaService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CuentaController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
))
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CuentaService cuentaService;

    @MockitoBean
    private CuentaMapper cuentaMapper;

    @Test
    void deberiaCrearCuenta() throws Exception {

        CuentaRequestDTO request = new CuentaRequestDTO(1L);

        Cuenta cuenta = new Cuenta();

        CuentaResponseDTO response = new CuentaResponseDTO(
                1L,
                "12345678901234567890",
                BigDecimal.ZERO,
                EstadoCuenta.ACTIVA,
                LocalDateTime.now(),
                new ClienteSimpleDTO(
                        1L,
                        "Alfonso",
                        "Perez"
                )
        );

        when(cuentaService.crearCuenta(anyLong())).thenReturn(cuenta);
        when(cuentaMapper.toResponse(cuenta)).thenReturn(response);

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroCuenta").value("12345678901234567890"))
                .andExpect(jsonPath("$.estado").value("ACTIVA"));

    }

    @Test
    void deberiaObtenerCuenta() throws Exception {

        Cuenta cuenta = new Cuenta();

        CuentaResponseDTO response = new CuentaResponseDTO(
                1L,
                "12345678901234567890",
                BigDecimal.valueOf(2500),
                EstadoCuenta.ACTIVA,
                LocalDateTime.now(),
                new ClienteSimpleDTO(
                        1L,
                        "Alfonso",
                        "Perez"
                )
        );

        when(cuentaService.obtenerCuenta("12345678901234567890"))
                .thenReturn(cuenta);

        when(cuentaMapper.toResponse(cuenta))
                .thenReturn(response);

        mockMvc.perform(get("/api/cuentas/12345678901234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta")
                        .value("12345678901234567890"))
                .andExpect(jsonPath("$.saldo")
                        .value(2500));

    }

    @Test
    void deberiaConsultarSaldo() throws Exception {

        when(cuentaService.consultarSaldo("12345678901234567890"))
                .thenReturn(BigDecimal.valueOf(5000));

        mockMvc.perform(get("/api/cuentas/12345678901234567890/saldo"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));

    }

    @Test
    void noDeberiaCrearCuentaSinCliente() throws Exception {

        CuentaRequestDTO request = new CuentaRequestDTO(null);

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

}
