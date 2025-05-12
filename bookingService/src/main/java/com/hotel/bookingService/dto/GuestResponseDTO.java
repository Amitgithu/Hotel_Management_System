package com.hotel.bookingService.dto;

import com.hotel.bookingService.model.Guest.IdProofType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestResponseDTO {
    private Long guestId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String nationality;
    private IdProofType idProofType;
    private String idProofNumber;
    private Instant createdAt;
}
