package com.hotel.staffService.service;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;

import java.util.List;

public interface StaffService {

    StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO);

    StaffResponseDTO getStaffById(Long id);

    List<StaffResponseDTO> getAllStaff();

    StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO);

    void deleteStaff(Long id);
}
