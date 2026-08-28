package com.banco.api.integration;

import com.banco.api.dto.cliente.ClienteRequestDTO;
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

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@Transactional
class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaCrearYObtenerClienteCorrectamente() throws Exception {

        ClienteRequestDTO request = new ClienteRequestDTO(
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        String respuesta = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Alfonso"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.documento").value("12345678A"))
                .andExpect(jsonPath("$.email").value("alfonso@test.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(respuesta);

        Long id = json.get("id").asLong();

        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Alfonso"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.documento").value("12345678A"))
                .andExpect(jsonPath("$.email").value("alfonso@test.com"));

    }

    @Test
    void deberiaListarClientes() throws Exception {

        ClienteRequestDTO request = new ClienteRequestDTO(
                "Juan",
                "Lopez",
                "87654321B",
                "juan@test.com"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].apellido").exists())
                .andExpect(jsonPath("$[0].documento").exists())
                .andExpect(jsonPath("$[0].email").exists());

    }

}