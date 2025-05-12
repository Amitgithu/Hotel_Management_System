package com.hotel.roomService.service.impl;

import com.hotel.roomService.dto.InventoryRequestDTO;
import com.hotel.roomService.dto.InventoryResponseDTO;
import com.hotel.roomService.exception.ResourceNotFoundException;
import com.hotel.roomService.model.InventoryItem;
import com.hotel.roomService.model.Room;
import com.hotel.roomService.repository.InventoryRepository;
import com.hotel.roomService.repository.RoomRepository;
import com.hotel.roomService.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private InventoryRequestDTO inventoryRequestDTO;
    private InventoryItem inventoryItem;
    private Room room;

    @BeforeEach
    void setUp() {
        // Setup for creating InventoryItem
        inventoryRequestDTO = new InventoryRequestDTO();
        inventoryRequestDTO.setItemName("Towel");
        inventoryRequestDTO.setCategory("Bathroom");
        inventoryRequestDTO.setQuantity(50);
        inventoryRequestDTO.setRoomId(1L);

        // Room mock setup
        room = new Room();
        room.setRoomId(1L);
        room.setRoomType("Deluxe");

        // InventoryItem mock setup
        inventoryItem = new InventoryItem();
        inventoryItem.setId(1L);
        inventoryItem.setItemName("Towel");
        inventoryItem.setCategory("Bathroom");
        inventoryItem.setQuantity(50);
        inventoryItem.setRoom(room);
    }

    @Test
    void testCreateItem() {
        // Mock Room repository behavior
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        // Call the create method
        InventoryItem createdItem = inventoryService.createItem(inventoryRequestDTO);

        // Verify results
        assertNotNull(createdItem);
        assertEquals("Towel", createdItem.getItemName());
        assertEquals("Bathroom", createdItem.getCategory());
        assertEquals(50, createdItem.getQuantity());
        assertEquals(1L, createdItem.getRoom().getRoomId());

        // Verify interactions
        verify(roomRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void testCreateItem_RoomNotFound() {
        // Mock Room repository to return empty (room not found)
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        // Call createItem and expect exception
        assertThrows(ResourceNotFoundException.class, () -> inventoryService.createItem(inventoryRequestDTO));

        // Verify interactions
        verify(roomRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(0)).save(any(InventoryItem.class));
    }

    @Test
    void testGetItemById() {
        // Mock the repository behavior
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));

        // Call the getItemById method
        InventoryResponseDTO response = inventoryService.getItemById(1L);

        // Verify results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Towel", response.getItemName());
        assertEquals("Bathroom", response.getCategory());
        assertEquals(50, response.getQuantity());
        assertEquals(1L, response.getRoomId());

        // Verify interactions
        verify(inventoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetItemById_ItemNotFound() {
        // Mock the repository to return empty (item not found)
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Call getItemById and expect exception
        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getItemById(1L));

        // Verify interactions
        verify(inventoryRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateItem() {
        // Mock Room repository behavior
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        // Call updateItem
        InventoryItem updatedItem = inventoryService.updateItem(1L, inventoryRequestDTO);

        // Verify results
        assertNotNull(updatedItem);
        assertEquals("Towel", updatedItem.getItemName());
        assertEquals("Bathroom", updatedItem.getCategory());
        assertEquals(50, updatedItem.getQuantity());
        assertEquals(1L, updatedItem.getRoom().getRoomId());

        // Verify interactions
        verify(roomRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void testUpdateItem_RoomNotFound() {
        // Mock Room repository behavior
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));

        // Call updateItem and expect exception
        assertThrows(ResourceNotFoundException.class, () -> inventoryService.updateItem(1L, inventoryRequestDTO));

        // Verify interactions
        verify(roomRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(0)).save(any(InventoryItem.class));
    }
}