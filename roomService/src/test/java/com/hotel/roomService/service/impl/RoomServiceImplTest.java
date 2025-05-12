package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.RoomRequestDto;
import com.hotel.roomService.dto.RoomResponseDto;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.repository.RoomRepository;
import com.hotel.roomService.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoom() {
        // Arrange
        RoomRequestDto requestDto = new RoomRequestDto("101", "Single", 100.0, true);
        Room room = new Room();
        Room savedRoom = new Room();
        RoomResponseDto responseDto = new RoomResponseDto();

        when(modelMapper.map(requestDto, Room.class)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(savedRoom);
        when(modelMapper.map(savedRoom, RoomResponseDto.class)).thenReturn(responseDto);

        // Act
        RoomResponseDto result = roomServiceImpl.createRoom(requestDto);

        // Assert
        assertNotNull(result);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testGetRoomById_Success() {
        // Arrange
        Long roomId = 1L;
        Room room = new Room();
        RoomResponseDto responseDto = new RoomResponseDto();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(modelMapper.map(room, RoomResponseDto.class)).thenReturn(responseDto);

        // Act
        RoomResponseDto result = roomServiceImpl.getRoomById(roomId);

        // Assert
        assertNotNull(result);
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testGetRoomById_NotFound() {
        // Arrange
        Long roomId = 1L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roomServiceImpl.getRoomById(roomId));
    }

    @Test
    void testGetAllRooms() {
        // Arrange
        List<Room> rooms = Arrays.asList(new Room(), new Room());
        when(roomRepository.findAll()).thenReturn(rooms);
        when(modelMapper.map(any(Room.class), eq(RoomResponseDto.class)))
                .thenReturn(new RoomResponseDto());

        // Act
        List<RoomResponseDto> result = roomServiceImpl.getAllRooms();

        // Assert
        assertEquals(2, result.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testUpdateRoom_Success() {
        // Arrange
        Long roomId = 1L;
        RoomRequestDto requestDto = new RoomRequestDto("101", "Double", 150.0, true);
        Room existingRoom = new Room();
        Room updatedRoom = new Room();
        RoomResponseDto responseDto = new RoomResponseDto();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(existingRoom));
        doAnswer(invocation -> null).when(modelMapper).map(eq(requestDto), eq(existingRoom));
        when(roomRepository.save(existingRoom)).thenReturn(updatedRoom);
        when(modelMapper.map(updatedRoom, RoomResponseDto.class)).thenReturn(responseDto);

        // Act
        RoomResponseDto result = roomServiceImpl.updateRoom(roomId, requestDto);

        // Assert
        assertNotNull(result);
        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).save(existingRoom);
    }

    @Test
    void testUpdateRoom_NotFound() {
        // Arrange
        Long roomId = 1L;
        RoomRequestDto requestDto = new RoomRequestDto("101", "Double", 150.0, true);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roomServiceImpl.updateRoom(roomId, requestDto));
    }

    @Test
    void testDeleteRoom_Success() {
        // Arrange
        Long roomId = 1L;
        Room room = new Room();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        roomServiceImpl.deleteRoom(roomId);

        // Assert
        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void testDeleteRoom_NotFound() {
        // Arrange
        Long roomId = 1L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roomServiceImpl.deleteRoom(roomId));
    }
}