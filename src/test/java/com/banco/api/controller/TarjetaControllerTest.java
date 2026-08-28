package com.banco.api.controller;

import com.banco.api.config.TestSecurityConfig;
import com.banco.api.dto.tarjeta.TarjetaRequestDTO;
import com.banco.api.dto.tarjeta.TarjetaResponseDTO;
import com.banco.api.entity.Tarjeta;
import com.banco.api.enums.EstadoTarjeta;
import com.banco.api.enums.TipoTarjeta;
import com.banco.api.mapper.TarjetaMapper;
import com.banco.api.security.SecurityConfig;
import com.banco.api.service.TarjetaService;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TarjetaController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
))
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class TarjetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TarjetaService tarjetaService;

    @MockitoBean
    private TarjetaMapper tarjetaMapper;

    @Test
    void deberiaCrearTarjeta() throws Exception {

        TarjetaRequestDTO request = new TarjetaRequestDTO(
                1L,
                TipoTarjeta.DEBITO
        );

        Tarjeta tarjeta = new Tarjeta();

        TarjetaResponseDTO response = new TarjetaResponseDTO(
                1L,
                "**** **** **** 1234",
                "ALFONSO PEREZ",
                TipoTarjeta.DEBITO,
                EstadoTarjeta.ACTIVA,
                LocalDate.now().plusYears(5)
        );

        when(tarjetaService.crearTarjeta(anyLong(), any(TipoTarjeta.class)))
                .thenReturn(tarjeta);

        when(tarjetaMapper.toResponse(tarjeta))
                .thenReturn(response);

        mockMvc.perform(post("/api/tarjetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroTarjeta")
                        .value("**** **** **** 1234"))
                .andExpect(jsonPath("$.estado")
                        .value("ACTIVA"));

    }

    @Test
    void deberiaObtenerTarjeta() throws Exception {

        Tarjeta tarjeta = new Tarjeta();

        TarjetaResponseDTO response = new TarjetaResponseDTO(
                1L,
                "**** **** **** 1234",
                "ALFONSO PEREZ",
                TipoTarjeta.CREDITO,
                EstadoTarjeta.ACTIVA,
                LocalDate.now().plusYears(5)
        );

        when(tarjetaService.obtenerTarjeta("1234567812345678"))
                .thenReturn(tarjeta);

        when(tarjetaMapper.toResponse(tarjeta))
                .thenReturn(response);

        mockMvc.perform(get("/api/tarjetas/1234567812345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroTarjeta")
                        .value("**** **** **** 1234"))
                .andExpect(jsonPath("$.tipo")
                        .value("CREDITO"));

    }

    @Test
    void deberiaBloquearTarjeta() throws Exception {

        Tarjeta tarjeta = new Tarjeta();

        TarjetaResponseDTO response = new TarjetaResponseDTO(
                1L,
                "**** **** **** 1234",
                "ALFONSO PEREZ",
                TipoTarjeta.DEBITO,
                EstadoTarjeta.BLOQUEADA,
                LocalDate.now().plusYears(5)
        );

        when(tarjetaService.bloquearTarjeta("1234567812345678"))
                .thenReturn(tarjeta);

        when(tarjetaMapper.toResponse(tarjeta))
                .thenReturn(response);

        mockMvc.perform(patch("/api/tarjetas/1234567812345678/bloquear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("BLOQUEADA"));

    }

    @Test
    void noDeberiaCrearTarjetaSinCuenta() throws Exception {

        TarjetaRequestDTO request = new TarjetaRequestDTO(
                null,
                TipoTarjeta.DEBITO
        );

        mockMvc.perform(post("/api/tarjetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void noDeberiaCrearTarjetaSinTipo() throws Exception {

        TarjetaRequestDTO request = new TarjetaRequestDTO(
                1L,
                null
        );

        mockMvc.perform(post("/api/tarjetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

}