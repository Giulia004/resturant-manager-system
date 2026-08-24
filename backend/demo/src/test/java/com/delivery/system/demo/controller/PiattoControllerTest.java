package com.delivery.system.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.delivery.system.demo.model.Categoria;
import com.delivery.system.demo.model.Piatto;
import com.delivery.system.demo.repository.UtenteRepository;
import com.delivery.system.demo.service.PiattoService;
import com.delivery.system.demo.security.JwtAuthFilter;
import com.delivery.system.demo.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PiattoController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class PiattoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PiattoService piattoService;

    @MockBean
    private UtenteRepository utenteRepository;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Piatto piattoSample;

    @BeforeEach
    void setUp() {
        piattoSample = new Piatto();
        piattoSample.setId(1L);
        piattoSample.setNome("Pizza Margherita");
        piattoSample.setPrezzo(6.50);
        piattoSample.setDescrizione("Pomodoro, mozzarella, basilico");
        piattoSample.setDisponibile(true);
        piattoSample.setCategoria(Categoria.PIZZE);
    }

    @Test
    void testGetAllPiatti() throws Exception {
        List<Piatto> lista = Arrays.asList(piattoSample);
        when(piattoService.getAllPiatti()).thenReturn(lista);

        mockMvc.perform(get("/api/piatti")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Pizza Margherita"));
    }

    @Test
    void testGetPiattoById_Success() throws Exception {
        when(piattoService.findPiattoById(1L)).thenReturn(piattoSample);

        mockMvc.perform(get("/api/piatti/{id}", 1L)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pizza Margherita"));
    }

    @Test
    void testGetPiattoById_NotFound() throws Exception {
        when(piattoService.findPiattoById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Il piatto non esiste"));

        mockMvc.perform(get("/api/piatti/{id}", 99L)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Il piatto non esiste"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_Success() throws Exception {
        Piatto nuovoPiatto = new Piatto();
        nuovoPiatto.setNome("Pizza Margherita");
        nuovoPiatto.setPrezzo(6.50);
        nuovoPiatto.setDescrizione("Pomodoro, mozzarella, basilico");
        nuovoPiatto.setDisponibile(true);
        nuovoPiatto.setCategoria(Categoria.PIZZE);

        when(piattoService.create(any(Piatto.class))).thenReturn(piattoSample);

        mockMvc.perform(post("/api/piatti").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuovoPiatto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.nome").value("Pizza Margherita"));
    }
}