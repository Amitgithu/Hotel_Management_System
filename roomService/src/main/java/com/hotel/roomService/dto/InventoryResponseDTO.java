package com.hotel.roomService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponseDTO {
    private Long id;
    private String itemName;
    private String category;
    private Integer quantity;
    private Long roomId;
}

