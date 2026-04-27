package com.julian.spring_demo.controller;

import com.julian.spring_demo.SpringDemoApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julian.spring_demo.dto.SupplierRequestDTO;
import com.julian.spring_demo.dto.SupplierResponseDTO;
import com.julian.spring_demo.exception.SupplierNotFoundException;
import com.julian.spring_demo.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest (classes = SpringDemoApplication.class)
@AutoConfigureMockMvc
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SupplierService supplierService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        SupplierResponseDTO dto = new SupplierResponseDTO(1L, "TechCorp", "tech@corp.com", "3001234567", 0);
        when(supplierService.getAll(any(), anyInt(), anyInt(), anyString()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("TechCorp"));
    }

    @Test
    void create_shouldReturn201() throws Exception {
        SupplierRequestDTO request = new SupplierRequestDTO();
        request.setName("TechCorp");
        request.setEmail("tech@corp.com");
        request.setPhone("3001234567");

        SupplierResponseDTO response = new SupplierResponseDTO(1L, "TechCorp", "tech@corp.com", "3001234567", 0);
        when(supplierService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(supplierService.getById(999L))
                .thenThrow(new SupplierNotFoundException(999L));

        mockMvc.perform(get("/api/suppliers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn400_whenEmailAlreadyExists() throws Exception {
        SupplierRequestDTO request = new SupplierRequestDTO();
        request.setName("TechCorp");
        request.setEmail("tech@corp.com");
        request.setPhone("3001234567");

        when(supplierService.create(any()))
                .thenThrow(new IllegalArgumentException("Supplier with this email already exists"));

        mockMvc.perform(post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
