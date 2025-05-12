package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.dto.InventoryItemShortDTO;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.repository.InventoryRepository;
import com.hotel.roomService.repository.RoomRepository;
import com.hotel.roomService.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public InventoryItem createItem(InventoryRequestDTO dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + dto.getRoomId()));

        InventoryItem item = new InventoryItem();
        item.setItemName(dto.getItemName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setRoom(room);

        return inventoryRepository.save(item);
    }

    @Override
    public List<InventoryResponseDTO> getAllItems() {
        return inventoryRepository.findAll().stream().map(item -> {
            InventoryResponseDTO dto = new InventoryResponseDTO();
            dto.setId(item.getId());
            dto.setItemName(item.getItemName());
            dto.setCategory(item.getCategory());
            dto.setQuantity(item.getQuantity());
            dto.setRoomId(item.getRoom().getRoomId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDTO getItemById(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id " + id));

        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setRoomId(item.getRoom().getRoomId());

        return dto;
    }

    @Override
    public InventoryItem updateItem(Long id, InventoryRequestDTO dto) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id " + id));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + dto.getRoomId()));

        item.setItemName(dto.getItemName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setRoom(room);

        return inventoryRepository.save(item);
    }
}
