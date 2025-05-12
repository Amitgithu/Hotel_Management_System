package com.hotel.bookingService.service.imple;

import com.hotel.bookingService.dto.GuestRequestDTO;
import com.hotel.bookingService.dto.GuestResponseDTO;
import com.hotel.bookingService.exception.GuestNotFoundException;
import com.hotel.bookingService.model.Guest;
import com.hotel.bookingService.repository.GuestRepository;
import com.hotel.bookingService.service.GuestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public GuestResponseDTO createGuest(GuestRequestDTO guestRequestDTO) {
        Guest guest = new Guest();

        guest.setFullName(guestRequestDTO.getFullName());
        guest.setEmail(guestRequestDTO.getEmail());
        guest.setPhone(guestRequestDTO.getPhone());
        guest.setAddress(guestRequestDTO.getAddress());
        guest.setNationality(guestRequestDTO.getNationality());
        guest.setIdProofType(guestRequestDTO.getIdProofType());
        guest.setIdProofNumber(guestRequestDTO.getIdProofNumber());

        Guest savedGuest = guestRepository.save(guest);
        return modelMapper.map(savedGuest, GuestResponseDTO.class);
    }

    @Override
    public GuestResponseDTO getGuestById(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with id: " + guestId));
        return modelMapper.map(guest, GuestResponseDTO.class);
    }

    @Override
    public List<GuestResponseDTO> getAllGuests() {
        return guestRepository.findAll()
                .stream()
                .map(guest -> modelMapper.map(guest, GuestResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public GuestResponseDTO updateGuest(Long guestId, GuestRequestDTO guestRequestDTO) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with id: " + guestId));

        guest.setFullName(guestRequestDTO.getFullName());
        guest.setEmail(guestRequestDTO.getEmail());
        guest.setPhone(guestRequestDTO.getPhone());
        guest.setAddress(guestRequestDTO.getAddress());
        guest.setNationality(guestRequestDTO.getNationality());
        guest.setIdProofType(guestRequestDTO.getIdProofType());
        guest.setIdProofNumber(guestRequestDTO.getIdProofNumber());

        Guest updatedGuest = guestRepository.save(guest);

        return modelMapper.map(updatedGuest, GuestResponseDTO.class);
    }

    @Override
    public void deleteGuest(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with id: " + guestId));
        guestRepository.delete(guest);
    }
}

