package com.hotel.staffService.service;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.exception.StaffNotFoundException;
import com.hotel.staffService.model.Staff;
import com.hotel.staffService.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceImplTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private ModelMapper modelMapper;

    private StaffRequestDTO staffRequestDTO;
    private StaffResponseDTO staffResponseDTO;
    private Staff staff;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        staffRequestDTO = StaffRequestDTO.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .salary(50000.0)
                .age(30)
                .occupation("Manager")
                .idProof("Passport")
                .idProofNumber("XYZ12345")
                .phoneNumber("1234567890")
                .joiningDate(LocalDate.now())
                .department("HR")
                .build();

        staffResponseDTO = StaffResponseDTO.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .salary(50000.0)
                .age(30)
                .occupation("Manager")
                .idProof("Passport")
                .idProofNumber("XYZ12345")
                .phoneNumber("1234567890")
                .joiningDate(LocalDate.now())
                .department("HR")
                .build();

        staff = Staff.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .salary(50000.0)
                .age(30)
                .occupation("Manager")
                .idProof("Passport")
                .idProofNumber("XYZ12345")
                .phoneNumber("1234567890")
                .joiningDate(LocalDate.now())
                .department("HR")
                .build();
    }

    @Test
    void createStaff() {
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        StaffResponseDTO response = staffService.createStaff(staffRequestDTO);

        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
        verify(staffRepository, times(1)).save(any(Staff.class));
    }

    @Test
    void getStaffById_ShouldReturnStaff_WhenStaffExists() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        StaffResponseDTO response = staffService.getStaffById(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
    }

    @Test
    void getStaffById_ShouldThrowException_WhenStaffNotFound() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.getStaffById(1L));
    }

    @Test
    void updateStaff_ShouldUpdateStaff_WhenStaffExists() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);
        when(modelMapper.map(staff, StaffResponseDTO.class)).thenReturn(staffResponseDTO);

        StaffResponseDTO response = staffService.updateStaff(1L, staffRequestDTO);

        assertNotNull(response);
        verify(staffRepository, times(1)).save(any(Staff.class));
    }

    @Test
    void updateStaff_ShouldThrowException_WhenStaffNotFound() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.updateStaff(1L, staffRequestDTO));
    }

    @Test
    void deleteStaff_ShouldDeleteStaff_WhenStaffExists() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));

        staffService.deleteStaff(1L);

        verify(staffRepository, times(1)).delete(staff);
    }

    @Test
    void deleteStaff_ShouldThrowException_WhenStaffNotFound() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> staffService.deleteStaff(1L));
    }
}