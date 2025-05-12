package com.hotel.bookingService.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long bookingId;

    private GuestResponseDTO guest;

    private RoomResponseDto room;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private Integer numGuests;

    private String bookingStatus;

    public BookingResponseDTO(String message) {
        this.bookingStatus = message; // Use bookingStatus to store the fallback message
    }
}
