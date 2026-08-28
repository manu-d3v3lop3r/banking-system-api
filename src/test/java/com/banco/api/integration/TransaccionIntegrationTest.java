package com.banco.api.integration;

import com.banco.api.dto.DepositoRequest;
import com.banco.api.dto.RetiroRequest;
import com.banco.api.dto.TransferenciaRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@Transactional
class TransaccionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaDepositarRetirarYTransferirCorrectamente() throws Exception {

        ClienteRequestDTO cliente1 = new ClienteRequestDTO(
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        ClienteRequestDTO cliente2 = new ClienteRequestDTO(
                "Juan",
                "Lopez",
                "87654321B",
                "juan@test.com"
        );

        String respuestaCliente1 = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String respuestaCliente2 = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long clienteId1 = objectMapper.readTree(respuestaCliente1).get("id").asLong();
        Long clienteId2 = objectMapper.readTree(respuestaCliente2).get("id").asLong();

        CuentaRequestDTO cuenta1 = new CuentaRequestDTO(clienteId1);
        CuentaRequestDTO cuenta2 = new CuentaRequestDTO(clienteId2);

        String respuestaCuenta1 = mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String respuestaCuenta2 = mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonCuenta1 = objectMapper.readTree(respuestaCuenta1);
        JsonNode jsonCuenta2 = objectMapper.readTree(respuestaCuenta2);

        String numeroCuenta1 = jsonCuenta1.get("numeroCuenta").asText();
        String numeroCuenta2 = jsonCuenta2.get("numeroCuenta").asText();

        System.out.println("Número cuenta 1: " + numeroCuenta1);
        System.out.println("Número cuenta 2: " + numeroCuenta2);

        DepositoRequest deposito = new DepositoRequest(numeroCuenta1, BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/transacciones/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposito)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("DEPOSITO"));

        RetiroRequest retiro = new RetiroRequest(numeroCuenta1, BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/transacciones/retiro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retiro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("RETIRO"));

        TransferenciaRequest transferencia = new TransferenciaRequest(numeroCuenta1, numeroCuenta2, BigDecimal.valueOf(300));

        mockMvc.perform(post("/api/transacciones/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferencia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("TRANSFERENCIA"));

    }

}