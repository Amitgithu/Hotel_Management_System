package com.hotel.bookingService.service.impl;

import com.hotel.bookingService.dto.*;
import com.hotel.bookingService.exception.*;
import com.hotel.bookingService.model.*;
import com.hotel.bookingService.repository.*;
import com.hotel.bookingService.service.GuestService;
import com.hotel.bookingService.service.RoomServiceClient;
import com.hotel.bookingService.service.imple.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomServiceClient roomServiceClient;

    @Mock
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequestDTO bookingRequestDTO;
    private GuestRequestDTO guestRequestDTO;
    private RoomResponseDto room;
    private GuestResponseDTO guestResponseDTO;
    private Guest guest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        guestRequestDTO = new GuestRequestDTO(
                "John Doe",
                "john@example.com",
                "1234567890",
                "123 Main St",
                "American",
                Guest.IdProofType.PASSPORT,
                "XYZ12345"
        );

        bookingRequestDTO = new BookingRequestDTO(
                guestRequestDTO,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                2
                );


        room = new RoomResponseDto(
                1L,
                "101",
                "DELUXE",
                150.0,
                true,
                Instant.now()
        );

        guestResponseDTO = new GuestResponseDTO(
                1L,
                "John Doe",
                "john@example.com",
                "1234567890",       // Phone
                "123 Main St",      // Address
                "American",         // Nationality
                Guest.IdProofType.PASSPORT,  // ID Proof Type
                "XYZ12345",         // ID Proof Number
                Instant.now()       // Created At (using Instant)
        );

        guest = new Guest(
                1L,
                "John Doe",
                "john@example.com",
                "1234567890",       // Phone
                "123 Main St",      // Address
                "American",         // Nationality
                Guest.IdProofType.PASSPORT,  // ID Proof Type
                "XYZ12345",         // ID Proof Number
                Instant.now()       // Created At (using Instant)
        ); }


    @Test
    void createBooking_success() {
        when(roomServiceClient.getAvailableRoomByType("DELUXE")).thenReturn(room);
        when(guestService.createGuest(guestRequestDTO)).thenReturn(guestResponseDTO);
        when(modelMapper.map(guestResponseDTO, Guest.class)).thenReturn(guest);

        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setRoomId(1L);
        booking.setGuestId(1L);
        booking.setCheckinDate(LocalDate.now());
        booking.setCheckoutDate(LocalDate.now().plusDays(2));
        booking.setNumGuests(2);
        booking.setBookingStatus(BookingStatus.PENDING);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO response = bookingService.createBooking("DELUXE", bookingRequestDTO);
        assertNotNull(response);
        assertEquals("PENDING", response.getBookingStatus());
    }

    @Test
    void createBooking_roomNotAvailable_throwsException() {
        when(roomServiceClient.getAvailableRoomByType("DELUXE")).thenReturn(null);
        assertThrows(RoomNotAvailableException.class, () -> bookingService.createBooking("DELUXE", bookingRequestDTO));
    }

    @Test
    void createBooking_guestDetailsMissing_throwsException() {
        when(roomServiceClient.getAvailableRoomByType("DELUXE")).thenReturn(room);
        bookingRequestDTO.setGuest(null);
        assertThrows(GuestDetailsMissingException.class, () -> bookingService.createBooking("DELUXE", bookingRequestDTO));
    }

    @Test
    void getBookingById_success() {
        Booking booking = new Booking(1L, 1L, 1L, BookingStatus.PENDING, LocalDate.now(), LocalDate.now().plusDays(1), 2, LocalDateTime.now(), null);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(roomServiceClient.getRoomById(1L)).thenReturn(room);
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        BookingResponseDTO response = bookingService.getBookingById(1L);
        assertEquals(1L, response.getBookingId());
    }

    @Test
    void getBookingById_bookingNotFound_throwsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(1L));
    }

    @Test
    void updateBookingStatus_success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus(BookingStatus.PENDING);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.updateBookingStatus(1L);
        assertEquals(BookingStatus.BOOKED, booking.getBookingStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void deleteBooking_success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.deleteBooking(1L);
        verify(bookingRepository).delete(booking);
    }
}