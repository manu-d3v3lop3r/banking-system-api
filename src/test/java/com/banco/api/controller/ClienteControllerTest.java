package com.banco.api.controller;

import com.banco.api.config.TestSecurityConfig;
import com.banco.api.dto.cliente.ClienteRequestDTO;
import com.banco.api.dto.cliente.ClienteResponseDTO;
import com.banco.api.entity.Cliente;
import com.banco.api.mapper.ClienteMapper;
import com.banco.api.security.SecurityConfig;
import com.banco.api.service.ClienteService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClienteController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
))
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ClienteMapper clienteMapper;

    @Test
    void deberiaCrearCliente() throws Exception {

        ClienteRequestDTO request = new ClienteRequestDTO(
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        Cliente cliente = new Cliente();

        ClienteResponseDTO response = new ClienteResponseDTO(
                1L,
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        when(clienteMapper.toEntity(any())).thenReturn(cliente);
        when(clienteService.crearCliente(any())).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Alfonso"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.documento").value("12345678A"))
                .andExpect(jsonPath("$.email").value("alfonso@test.com"));
    }

    @Test
    void deberiaObtenerClientePorId() throws Exception {

        Cliente cliente = new Cliente();

        ClienteResponseDTO response = new ClienteResponseDTO(
                1L,
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        when(clienteService.obtenerClientePorId(1L)).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(response);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Alfonso"));
    }

    @Test
    void deberiaListarClientes() throws Exception {

        Cliente cliente = new Cliente();

        ClienteResponseDTO response = new ClienteResponseDTO(
                1L,
                "Alfonso",
                "Perez",
                "12345678A",
                "alfonso@test.com"
        );

        when(clienteService.listarClientes())
                .thenReturn(List.of(cliente));

        when(clienteMapper.toResponse(List.of(cliente)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Alfonso"))
                .andExpect(jsonPath("$[0].email").value("alfonso@test.com"));
    }

    @Test
    void noDeberiaCrearClienteConDatosInvalidos() throws Exception {

        ClienteRequestDTO request = new ClienteRequestDTO(
                "",
                "",
                "",
                "correo"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

}
