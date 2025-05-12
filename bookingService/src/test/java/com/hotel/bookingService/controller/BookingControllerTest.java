package com.hotel.bookingService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.bookingService.dto.*;
import com.hotel.bookingService.model.Guest;
import com.hotel.bookingService.service.BookingService;
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
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper; // Used to convert objects to JSON strings

    private GuestRequestDTO guestRequestDTO;
    private GuestResponseDTO guestResponseDTO;
    private RoomResponseDto roomResponseDto;
    private BookingRequestDTO bookingRequestDTO;
    private BookingResponseDTO bookingResponseDTO;

    @BeforeEach
    void setUp() {
        guestRequestDTO = new GuestRequestDTO(
                "John Doe",
                "john@example.com",
                "1234567890",
                "123 Main St",
                "American",
                Guest.IdProofType.PASSPORT,
                "XYZ12345"
        );

         guestResponseDTO = new GuestResponseDTO(
                1L,                     // Guest ID
                "John Doe",             // Full Name
                "john@example.com",     // Email
                "1234567890",           // Phone
                "123 Main St",          // Address
                "American",             // Nationality
                Guest.IdProofType.PASSPORT, // ID Proof Type
                "XYZ12345",             // ID Proof Number
                Instant.now()           // Created At Timestamp
        );

        roomResponseDto = new RoomResponseDto(
                1L,
                "101",
                "DELUXE",
                150.0,
                true,
                Instant.now()
        );

        bookingRequestDTO = new BookingRequestDTO(
                guestRequestDTO,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                2
        );

        bookingResponseDTO = new BookingResponseDTO(
                1L,
                guestResponseDTO,
                roomResponseDto,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                2,
                "CONFIRMED"
        );
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        Mockito.when(bookingService.createBooking(Mockito.eq("DELUXE"), Mockito.any(BookingRequestDTO.class)))
                .thenReturn(bookingResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings")
                        .param("roomType", "DELUXE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingStatus").value("CONFIRMED"));
    }

    @Test
    void testGetBookingById_Success() throws Exception {
        Mockito.when(bookingService.getBookingById(1L)).thenReturn(bookingResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingStatus").value("CONFIRMED"));
    }

    @Test
    void testGetAllBookings_Success() throws Exception {
        List<BookingResponseDTO> bookings = List.of(
                bookingResponseDTO,
                new BookingResponseDTO(2L, guestResponseDTO, roomResponseDto, LocalDate.now(), LocalDate.now().plusDays(3), 3, "PENDING")
        );

        Mockito.when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].bookingStatus").value("PENDING"));
    }

    @Test
    void testUpdateBooking_Success() throws Exception {
        BookingResponseDTO updatedResponseDTO = new BookingResponseDTO(
                1L, guestResponseDTO, roomResponseDto, LocalDate.now(), LocalDate.now().plusDays(4), 2, "UPDATED"
        );

        Mockito.when(bookingService.updateBooking(Mockito.eq(1L), Mockito.any(BookingRequestDTO.class)))
                .thenReturn(updatedResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingStatus").value("UPDATED"));
    }

    @Test
    void testDeleteBooking_Success() throws Exception {
        Mockito.doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking deleted successfully"));
    }
}