package com.hotel.staffService.service;

import com.hotel.staffService.dto.StaffRequestDTO;
import com.hotel.staffService.dto.StaffResponseDTO;
import com.hotel.staffService.exception.StaffNotFoundException;
import com.hotel.staffService.model.Staff;
import com.hotel.staffService.exception.ResourceNotFoundException;
import com.hotel.staffService.repository.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO) {

        Staff staff = new Staff();
        staff.setFullName(staffRequestDTO.getFullName());
        staff.setEmail(staffRequestDTO.getEmail());
        staff.setSalary(staffRequestDTO.getSalary());
        staff.setAddress(staffRequestDTO.getAddress());
        staff.setAge(staffRequestDTO.getAge());
        staff.setOccupation(staffRequestDTO.getOccupation());
        staff.setIdProof(staffRequestDTO.getIdProof());
        staff.setIdProofNumber(staffRequestDTO.getIdProofNumber());
        staff.setPhoneNumber(staffRequestDTO.getPhoneNumber());
        staff.setJoiningDate(staffRequestDTO.getJoiningDate());
        staff.setDepartment(staffRequestDTO.getDepartment());

        Staff savedStaff = staffRepository.save(staff);
        return modelMapper.map(savedStaff, StaffResponseDTO.class);
    }

    @Override
    public StaffResponseDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + id));
        return modelMapper.map(staff, StaffResponseDTO.class);
    }

    @Override
    public List<StaffResponseDTO> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();
        return staffList.stream()
                .map(staff -> modelMapper.map(staff, StaffResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + id));


        staff.setFullName(staffRequestDTO.getFullName());
        staff.setEmail(staffRequestDTO.getEmail());
        staff.setSalary(staffRequestDTO.getSalary());
        staff.setAddress(staffRequestDTO.getAddress());
        staff.setAge(staffRequestDTO.getAge());
        staff.setOccupation(staffRequestDTO.getOccupation());
        staff.setIdProof(staffRequestDTO.getIdProof());
        staff.setIdProofNumber(staffRequestDTO.getIdProofNumber());
        staff.setPhoneNumber(staffRequestDTO.getPhoneNumber());
        staff.setJoiningDate(staffRequestDTO.getJoiningDate());
        staff.setDepartment(staffRequestDTO.getDepartment());
        
        Staff updatedStaff = staffRepository.save(staff);

        return modelMapper.map(updatedStaff, StaffResponseDTO.class);
    }

    @Override
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + id));
        staffRepository.delete(staff);
    }
}
