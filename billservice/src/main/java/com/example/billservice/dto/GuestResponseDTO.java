package com.example.billservice.dto;

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

    public enum IdProofType {
        AADHAR,
        PASSPORT,
        DRIVING_LICENSE,
        OTHER
    }
}
