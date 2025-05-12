package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.InventoryItemShortDTO;
import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.exception.RoomAlreadyExistsException;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.repository.InventoryRepository;
import com.hotel.roomService.repository.RoomRepository;
import com.hotel.roomService.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        // Check if room number already exists
        if (roomRepository.findByRoomNumber(roomRequestDto.getRoomNumber()).isPresent()) {
            throw new RoomAlreadyExistsException("Room number " + roomRequestDto.getRoomNumber() + " already exists.");
        }

        // Map and save
        Room room = modelMapper.map(roomRequestDto, Room.class);
        room = roomRepository.save(room);
        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    public RoomResponseDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        RoomResponseDto dto = modelMapper.map(room, RoomResponseDto.class);
        List<InventoryItemShortDTO> inventories = room.getRoomInventories().stream()
                .map(item -> modelMapper.map(item, InventoryItemShortDTO.class))
                .collect(Collectors.toList());
        dto.setRoomInventories(inventories);
        return dto;
    }

    @Override
    public List<RoomResponseDto> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(room -> {
                    RoomResponseDto dto = modelMapper.map(room, RoomResponseDto.class);
                    List<InventoryItemShortDTO> inventories = room.getRoomInventories().stream()
                            .map(item -> modelMapper.map(item, InventoryItemShortDTO.class))
                            .collect(Collectors.toList());
                    dto.setRoomInventories(inventories);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDto findRoomByType(String roomType) {
        List<Room> rooms = roomRepository.findAvailableRoomByType(roomType);
        if (rooms.isEmpty()) {
            return null;
        }

        Room room = rooms.get(0);
        RoomResponseDto dto = modelMapper.map(room, RoomResponseDto.class);

        // Manually set roomType to ensure correct mapping
        dto.setRoomType(room.getRoomType());
        return dto;
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        roomRepository.delete(room);
    }


    @Override
    public void updateRoomAvailability(Long roomId, boolean isAvailable) {
        System.out.println("Update Room");
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        room.setAvailable(isAvailable);
        roomRepository.save(room);
    }

    @Override
    public RoomResponseDto updateRoom(Long roomId, RoomRequestDto roomRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        modelMapper.map(roomRequestDto, room);
        room = roomRepository.save(room);
        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    public void addInventoryToRoom(Long roomId, InventoryItem itemData) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        itemData.setRoom(room);
        inventoryRepository.save(itemData);
    }
}
