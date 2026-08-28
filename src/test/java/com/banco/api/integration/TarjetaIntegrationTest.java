package com.banco.api.integration;

import com.banco.api.dto.cliente.ClienteRequestDTO;
import com.banco.api.dto.cuenta.CuentaRequestDTO;
import com.banco.api.dto.tarjeta.TarjetaRequestDTO;
import com.banco.api.enums.TipoTarjeta;
import com.banco.api.repository.TarjetaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@Transactional
class TarjetaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Test
    void deberiaCrearConsultarYBloquearTarjeta() throws Exception {

        ClienteRequestDTO cliente = new ClienteRequestDTO(
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        String clienteResponse = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long clienteId = objectMapper.readTree(clienteResponse)
                .get("id")
                .asLong();

        CuentaRequestDTO cuenta = new CuentaRequestDTO(clienteId);

        String cuentaResponse = mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long cuentaId = objectMapper.readTree(cuentaResponse)
                .get("id")
                .asLong();

        TarjetaRequestDTO tarjeta = new TarjetaRequestDTO(
                cuentaId,
                TipoTarjeta.DEBITO
        );

        mockMvc.perform(post("/api/tarjetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarjeta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titular").value("ALFONSO PEREZ"))
                .andExpect(jsonPath("$.numeroTarjeta").exists())
                .andExpect(jsonPath("$.tipo").value("DEBITO"))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.fechaExpiracion").exists());

        String numeroTarjeta = tarjetaRepository.findAll()
                .getFirst()
                .getNumeroTarjeta();

        mockMvc.perform(get("/api/tarjetas/{numeroTarjeta}", numeroTarjeta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titular").value("ALFONSO PEREZ"))
                .andExpect(jsonPath("$.numeroTarjeta")
                        .value("**** **** **** " + numeroTarjeta.substring(12)))
                .andExpect(jsonPath("$.tipo").value("DEBITO"))
                .andExpect(jsonPath("$.estado").value("ACTIVA"));

        mockMvc.perform(patch("/api/tarjetas/{numeroTarjeta}/bloquear", numeroTarjeta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titular").value("ALFONSO PEREZ"))
                .andExpect(jsonPath("$.numeroTarjeta")
                        .value("**** **** **** " + numeroTarjeta.substring(12)))
                .andExpect(jsonPath("$.estado").value("BLOQUEADA"));

    }

}