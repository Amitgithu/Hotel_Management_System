package com.hotel.bookingService.controller;

import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;
import com.hotel.bookingService.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping
    public ResponseEntity<GuestResponseDTO> createGuest(@Valid @RequestBody GuestRequestDTO guestRequestDTO) {
        return ResponseEntity.ok(guestService.createGuest(guestRequestDTO));
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResponseDTO> getGuestById(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.getGuestById(guestId));
    }

    @GetMapping
    public ResponseEntity<List<GuestResponseDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @PutMapping("/{guestId}")
    public ResponseEntity<GuestResponseDTO> updateGuest(@PathVariable Long guestId,
                                                        @Valid @RequestBody GuestRequestDTO guestRequestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestId, guestRequestDTO));
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}
