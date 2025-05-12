package com.hotel.roomService.controller;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.service.RoomService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody RoomRequestDto roomRequestDto) {
        return ResponseEntity.ok(roomService.createRoom(roomRequestDto));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @GetMapping("/available")
    public ResponseEntity<RoomResponseDto> getAvailableRoomByType(@RequestParam String roomType) {
        RoomResponseDto room = roomService.findRoomByType(roomType);
        System.out.println("Finding rooms in ROOM_SERVICE");
        return ResponseEntity.ok(room);
    }

    @PutMapping("/{roomId}/availability")
    public ResponseEntity<Void> updateRoomAvailability(@PathVariable Long roomId,
                                                       @RequestParam boolean available) {
        roomService.updateRoomAvailability(roomId, available);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long roomId, @Valid @RequestBody RoomRequestDto roomRequestDto) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, roomRequestDto));
    }

}
