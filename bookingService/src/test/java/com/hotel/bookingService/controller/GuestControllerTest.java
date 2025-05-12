package com.hotel.bookingService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;
import com.hotel.bookingService.model.Guest.IdProofType;
import com.hotel.bookingService.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuestController.class)
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Autowired
    private ObjectMapper objectMapper; // Converts objects to JSON

    private GuestRequestDTO guestRequestDTO;
    private GuestResponseDTO guestResponseDTO;

    @BeforeEach
    void setUp() {
        guestRequestDTO = new GuestRequestDTO(
                "John Doe",
                "john@example.com",
                "1234567890",
                "123 Main St",
                "American",
                IdProofType.PASSPORT,
                "XYZ12345"
        );

        guestResponseDTO = new GuestResponseDTO(
                1L,
                "John Doe",
                "john@example.com",
                "1234567890",
                "123 Main St",
                "American",
                IdProofType.PASSPORT,
                "XYZ12345",
                Instant.now()
        );
    }

    @Test
    void testCreateGuest_Success() throws Exception {
        Mockito.when(guestService.createGuest(Mockito.any(GuestRequestDTO.class)))
                .thenReturn(guestResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetGuestById_Success() throws Exception {
        Mockito.when(guestService.getGuestById(1L)).thenReturn(guestResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void testGetAllGuests_Success() throws Exception {
        List<GuestResponseDTO> guests = List.of(guestResponseDTO);
        Mockito.when(guestService.getAllGuests()).thenReturn(guests);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    void testUpdateGuest_Success() throws Exception {
        Mockito.when(guestService.updateGuest(Mockito.eq(1L), Mockito.any(GuestRequestDTO.class)))
                .thenReturn(guestResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testDeleteGuest_Success() throws Exception {
        Mockito.doNothing().when(guestService).deleteGuest(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/guests/1"))
                .andExpect(status().isNoContent());
    }
}