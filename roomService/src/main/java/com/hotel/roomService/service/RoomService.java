package com.hotel.roomService.service;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.model.Room;

import java.util.List;

public interface RoomService {
    RoomResponseDto createRoom(RoomRequestDto roomRequestDto);
    RoomResponseDto getRoomById(Long roomId);
    List<RoomResponseDto> getAllRooms();
    RoomResponseDto findRoomByType(String roomType);
    void updateRoomAvailability(Long roomId, boolean isAvailable);
    RoomResponseDto updateRoom(Long roomId, RoomRequestDto roomRequestDto);
    void addInventoryToRoom(Long roomId, InventoryItem itemData);
}
