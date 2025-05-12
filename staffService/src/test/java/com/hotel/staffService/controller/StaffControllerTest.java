package com.hotel.staffService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StaffController.class)
class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffService staffService;

    @Autowired
    private ObjectMapper objectMapper;

    private StaffRequestDTO staffRequestDTO;
    private StaffResponseDTO staffResponseDTO;

    @BeforeEach
    void setUp() {
        staffRequestDTO = new StaffRequestDTO(
                "Alice Johnson",
                "alice@example.com",
                45000.0,
                "789 Park Ave",
                35,
                "Supervisor",
                "Aadhar",
                "AAD12345",
                "9988776655",
                LocalDate.now(),
                "Housekeeping"
        );

        staffResponseDTO = new StaffResponseDTO(
                "Alice Johnson",
                "alice@example.com",
                45000.0,
                "789 Park Ave",
                35,
                "Supervisor",
                "Aadhar",
                "AAD12345",
                "9988776655",
                LocalDate.now(),
                "Housekeeping"
        );
    }

    @Test
    void testCreateStaff_Success() throws Exception {
        Mockito.when(staffService.createStaff(Mockito.any(StaffRequestDTO.class)))
                .thenReturn(staffResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staffRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testGetStaffById_Success() throws Exception {
        Mockito.when(staffService.getStaffById(1L)).thenReturn(staffResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/staff/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Alice Johnson"));
    }

    @Test
    void testGetAllStaff_Success() throws Exception {
        List<StaffResponseDTO> staffList = List.of(staffResponseDTO);
        Mockito.when(staffService.getAllStaff()).thenReturn(staffList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/staff"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Alice Johnson"));
    }

    @Test
    void testUpdateStaff_Success() throws Exception {
        Mockito.when(staffService.updateStaff(Mockito.eq(1L), Mockito.any(StaffRequestDTO.class)))
                .thenReturn(staffResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/staff/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staffRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testDeleteStaff_Success() throws Exception {
        Mockito.doNothing().when(staffService).deleteStaff(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/staff/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Staff deleted successfully"));
    }
}