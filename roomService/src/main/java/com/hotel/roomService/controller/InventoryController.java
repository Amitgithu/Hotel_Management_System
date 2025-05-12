package com.hotel.roomService.controller;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public InventoryItem createItem(@RequestBody InventoryRequestDTO dto) {
        return inventoryService.createItem(dto);
    }

    @GetMapping
    public List<InventoryResponseDTO> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{id}")
    public InventoryResponseDTO getItem(@PathVariable Long id) {
        return inventoryService.getItemById(id);
    }

    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable Long id, @RequestBody InventoryRequestDTO dto) {
        return inventoryService.updateItem(id, dto);
    }
}
