package com.hotel.roomService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.service.RoomService;
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

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    private RoomRequestDto roomRequestDto;
    private RoomResponseDto roomResponseDto;

    @BeforeEach
    void setUp() {
        roomRequestDto = new RoomRequestDto("101", "DELUXE", 150.0, true);

        roomResponseDto = new RoomResponseDto(
                1L,
                "101",
                "DELUXE",
                150.0,
                true,
                Instant.now(),
                List.of()
        );
    }

    @Test
    void testCreateRoom_Success() throws Exception {
        Mockito.when(roomService.createRoom(Mockito.any(RoomRequestDto.class)))
                .thenReturn(roomResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("101"))
                .andExpect(jsonPath("$.roomType").value("DELUXE"))
                .andExpect(jsonPath("$.pricePerNight").value(150.0))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testGetRoomById_Success() throws Exception {
        Mockito.when(roomService.getRoomById(1L)).thenReturn(roomResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("101"))
                .andExpect(jsonPath("$.roomType").value("DELUXE"));
    }

    @Test
    void testGetAllRooms_Success() throws Exception {
        List<RoomResponseDto> roomList = List.of(roomResponseDto);
        Mockito.when(roomService.getAllRooms()).thenReturn(roomList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].roomNumber").value("101"));
    }

    @Test
    void testUpdateRoomAvailability_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/1/availability")
                        .param("available", "false"))
                .andExpect(status().isOk());

        Mockito.verify(roomService).updateRoomAvailability(1L, false);
    }

    @Test
    void testUpdateRoom_Success() throws Exception {
        Mockito.when(roomService.updateRoom(Mockito.eq(1L), Mockito.any(RoomRequestDto.class)))
                .thenReturn(roomResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("101"))
                .andExpect(jsonPath("$.roomType").value("DELUXE"));
    }
}