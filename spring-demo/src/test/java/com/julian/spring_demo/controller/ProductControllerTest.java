package com.julian.spring_demo.controller;

import com.julian.spring_demo.SpringDemoApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julian.spring_demo.dto.ProductRequestDTO;
import com.julian.spring_demo.dto.ProductResponseDTO;
import com.julian.spring_demo.exception.ProductNotFoundException;
import com.julian.spring_demo.service.ProductService;
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
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        ProductResponseDTO dto = new ProductResponseDTO(1L, "Laptop", 2500000, 10, "Electronics");
        when(productService.getAll(any(), any(), anyInt(), anyInt(), anyString()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Laptop"));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        ProductResponseDTO dto = new ProductResponseDTO(1L, "Laptop", 2500000, 10, "Electronics");
        when(productService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void create_shouldReturn201() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Laptop");
        request.setPrice(2500000);
        request.setStock(10);

        ProductResponseDTO response = new ProductResponseDTO(1L, "Laptop", 2500000, 10, null);
        when(productService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(productService.getById(999L))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}
