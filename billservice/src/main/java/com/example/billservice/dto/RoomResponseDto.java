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
public class RoomResponseDto {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
    private boolean isAvailable;
    private Instant createdAt;

}