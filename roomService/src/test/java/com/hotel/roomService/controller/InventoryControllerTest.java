package com.hotel.roomService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryRequestDTO inventoryRequestDTO;
    private InventoryResponseDTO inventoryResponseDTO;
    private InventoryItem inventoryItem;

    @BeforeEach
    void setUp() {
        inventoryRequestDTO = new InventoryRequestDTO("Towels", "Linen", 20, 1L);

        inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(1L);
        inventoryResponseDTO.setItemName("Towels");
        inventoryResponseDTO.setCategory("Linen");
        inventoryResponseDTO.setQuantity(20);
        inventoryResponseDTO.setRoomId(1L);

        inventoryItem = new InventoryItem();
        inventoryItem.setId(1L);
        inventoryItem.setItemName("Towels");
        inventoryItem.setCategory("Linen");
        inventoryItem.setQuantity(20);
    }

    @Test
    void testCreateItem_Success() throws Exception {
        Mockito.when(inventoryService.createItem(Mockito.any(InventoryRequestDTO.class))).thenReturn(inventoryItem);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Towels"))
                .andExpect(jsonPath("$.category").value("Linen"))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    void testGetItemById_Success() throws Exception {
        Mockito.when(inventoryService.getItemById(1L)).thenReturn(inventoryResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Towels"));
    }

    @Test
    void testGetAllItems_Success() throws Exception {
        List<InventoryResponseDTO> inventoryList = List.of(inventoryResponseDTO);
        Mockito.when(inventoryService.getAllItems()).thenReturn(inventoryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].itemName").value("Towels"));
    }

    @Test
    void testUpdateItem_Success() throws Exception {
        Mockito.when(inventoryService.updateItem(Mockito.eq(1L), Mockito.any(InventoryRequestDTO.class)))
                .thenReturn(inventoryItem);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Towels"))
                .andExpect(jsonPath("$.category").value("Linen"));
    }
}