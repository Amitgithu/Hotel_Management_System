package com.hotel.bookingService.service.impl;

import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;
import com.hotel.bookingService.exception.GuestNotFoundException;
import com.hotel.bookingService.exception.ResourceNotFoundException;
import com.hotel.bookingService.model.Guest;
import com.hotel.bookingService.repository.GuestRepository;
import com.hotel.bookingService.service.imple.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GuestServiceImpl guestServiceImpl;

    private Guest guest;
    private GuestRequestDTO guestRequestDTO;
    private GuestResponseDTO guestResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        guest = new Guest();
        guest.setGuestId(1L);
        guest.setFullName("John Doe");
        guest.setEmail("john.doe@example.com");
        guest.setPhone("1234567890");
        guest.setAddress("123 Main Street");
        guest.setNationality("American");
        guest.setIdProofType(Guest.IdProofType.PASSPORT);
        guest.setIdProofNumber("A1234567");

        guestRequestDTO = new GuestRequestDTO();
        guestRequestDTO.setFullName("John Doe");
        guestRequestDTO.setEmail("john.doe@example.com");
        guestRequestDTO.setPhone("1234567890");
        guestRequestDTO.setAddress("123 Main Street");
        guestRequestDTO.setNationality("American");
        guest.setIdProofType(Guest.IdProofType.PASSPORT);
        guestRequestDTO.setIdProofNumber("A1234567");

        guestResponseDTO = new GuestResponseDTO();
        guestResponseDTO.setGuestId(1L);
        guestResponseDTO.setFullName("John Doe");
        guestResponseDTO.setEmail("john.doe@example.com");
        guestResponseDTO.setPhone("1234567890");
        guestResponseDTO.setAddress("123 Main Street");
        guestResponseDTO.setNationality("American");
        guest.setIdProofType(Guest.IdProofType.PASSPORT);
        guestResponseDTO.setIdProofNumber("A1234567");
    }

    @Test
    void testCreateGuest() {
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(modelMapper.map(any(Guest.class), eq(GuestResponseDTO.class))).thenReturn(guestResponseDTO);

        GuestResponseDTO createdGuest = guestServiceImpl.createGuest(guestRequestDTO);

        assertNotNull(createdGuest);
        assertEquals("John Doe", createdGuest.getFullName());
        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void testGetGuestById_Success() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(modelMapper.map(guest, GuestResponseDTO.class)).thenReturn(guestResponseDTO);

        GuestResponseDTO result = guestServiceImpl.getGuestById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
    }

    @Test
    void testGetGuestById_NotFound() {
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestServiceImpl.getGuestById(1L));
    }

    @Test
    void testGetAllGuests() {
        when(guestRepository.findAll()).thenReturn(Arrays.asList(guest));
        when(modelMapper.map(any(Guest.class), eq(GuestResponseDTO.class))).thenReturn(guestResponseDTO);

        List<GuestResponseDTO> guests = guestServiceImpl.getAllGuests();

        assertEquals(1, guests.size());
        verify(guestRepository, times(1)).findAll();
    }

    @Test
    void testUpdateGuest_Success() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
        when(modelMapper.map(any(Guest.class), eq(GuestResponseDTO.class))).thenReturn(guestResponseDTO);

        GuestResponseDTO updatedGuest = guestServiceImpl.updateGuest(1L, guestRequestDTO);

        assertNotNull(updatedGuest);
        assertEquals("John Doe", updatedGuest.getFullName());
        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void testUpdateGuest_NotFound() {
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestServiceImpl.updateGuest(1L, guestRequestDTO));
    }

    @Test
    void testDeleteGuest_Success() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        doNothing().when(guestRepository).delete(guest);

        guestServiceImpl.deleteGuest(1L);

        verify(guestRepository, times(1)).delete(guest);
    }

    @Test
    void testDeleteGuest_NotFound() {
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestServiceImpl.deleteGuest(1L));
    }
}
