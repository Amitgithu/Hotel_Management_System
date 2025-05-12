package com.hotel.roomService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
    private Boolean available;
    private Instant createdAt;
    private List<InventoryItemShortDTO> roomInventories;

}
