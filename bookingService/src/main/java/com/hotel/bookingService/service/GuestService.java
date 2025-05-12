package com.hotel.bookingService.service;

import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;

import java.util.List;

public interface GuestService {
    GuestResponseDTO createGuest(GuestRequestDTO guestRequestDTO);
    GuestResponseDTO getGuestById(Long guestId);
    List<GuestResponseDTO> getAllGuests();
    GuestResponseDTO updateGuest(Long guestId, GuestRequestDTO guestRequestDTO);
    void deleteGuest(Long guestId);
}
