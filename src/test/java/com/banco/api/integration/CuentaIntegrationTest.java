package com.banco.api.integration;

import com.banco.api.dto.cliente.ClienteRequestDTO;
import com.banco.api.dto.cuenta.CuentaRequestDTO;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@Transactional
class CuentaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaCrearConsultarYConsultarSaldoCuenta() throws Exception {

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
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.numeroCuenta").exists())
                .andExpect(jsonPath("$.saldo").value(0))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode cuentaJson = objectMapper.readTree(cuentaResponse);

        String numeroCuenta = cuentaJson.get("numeroCuenta").asText();

        mockMvc.perform(get("/api/cuentas/{numeroCuenta}", numeroCuenta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.numeroCuenta").value(numeroCuenta))
                .andExpect(jsonPath("$.saldo").value(0))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.cliente.id").value(clienteId));

        mockMvc.perform(get("/api/cuentas/{numeroCuenta}/saldo", numeroCuenta))
                .andExpect(status().isOk())
                .andExpect(content().string(BigDecimal.ZERO.toString()));

    }

    @Test
    void deberiaCrearVariasCuentasParaClientesDistintos() throws Exception {

        ClienteRequestDTO cliente1 = new ClienteRequestDTO(
                "Juan",
                "Lopez",
                "11111111A",
                "juan@test.com"
        );

        ClienteRequestDTO cliente2 = new ClienteRequestDTO(
                "Pedro",
                "Garcia",
                "22222222B",
                "pedro@test.com"
        );

        String response1 = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String response2 = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id1 = objectMapper.readTree(response1).get("id").asLong();
        Long id2 = objectMapper.readTree(response2).get("id").asLong();

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CuentaRequestDTO(id1))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CuentaRequestDTO(id2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));

    }

}