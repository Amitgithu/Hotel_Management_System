package com.hotel.roomService.dto;

import lombok.Data;

@Data
public class InventoryItemShortDTO {
    private Long id;
    private String itemName;
    private String category;
    private int quantity;
}

