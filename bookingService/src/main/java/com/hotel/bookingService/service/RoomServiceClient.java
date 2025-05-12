package com.hotel.bookingService.service;

import com.hotel.bookingService.dto.RoomResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ROOM-SERVICE") // replace with your RoomService URL/Service Registry name
public interface RoomServiceClient {

    @GetMapping("/api/rooms/available")
    RoomResponseDto getAvailableRoomByType(@RequestParam("roomType") String roomType);

    @PutMapping("/api/rooms/{roomId}/availability")
    void updateRoomAvailability(@PathVariable("roomId") Long roomId,
                                @RequestParam("available") boolean available);

    @GetMapping("/api/rooms/{roomId}")
    RoomResponseDto getRoomById(@PathVariable("roomId") Long roomId);

}
