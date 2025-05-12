package com.hotel.roomService.service;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.model.InventoryItem;

import java.util.List;

public interface InventoryService {
    InventoryItem createItem(InventoryRequestDTO dto);
    List<InventoryResponseDTO> getAllItems();
    InventoryResponseDTO getItemById(Long id);
    InventoryItem updateItem(Long id, InventoryRequestDTO dto);
}
