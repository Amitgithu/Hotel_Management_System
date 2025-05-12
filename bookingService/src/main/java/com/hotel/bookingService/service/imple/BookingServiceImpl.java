package com.hotel.bookingService.service.imple;

import com.hotel.bookingService.dto.*;
import com.hotel.bookingService.exception.*;
import com.hotel.bookingService.model.Booking;
import com.hotel.bookingService.model.BookingStatus;
import com.hotel.bookingService.model.Guest;
import com.hotel.bookingService.repository.BookingRepository;
import com.hotel.bookingService.repository.GuestRepository;
import com.hotel.bookingService.service.BookingService;
import com.hotel.bookingService.service.GuestService;
import com.hotel.bookingService.service.RoomServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomServiceClient roomServiceClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GuestService guestService;

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public BookingResponseDTO createBooking(String roomType, BookingRequestDTO bookingRequestDTO) {
        RoomResponseDto room = roomServiceClient.getAvailableRoomByType(roomType);

        if (room == null || !room.isAvailable()) {
            throw new RoomNotAvailableException("No available room of type: " + roomType);
        }


        GuestRequestDTO guestDetail = bookingRequestDTO.getGuest();
        if (guestDetail == null) {
            throw new GuestDetailsMissingException("Guest details are missing.");
        }


        GuestResponseDTO guestResponse = guestService.createGuest(guestDetail);
        Guest guestCreated = modelMapper.map(guestResponse, Guest.class);

        Booking booking = new Booking();
        booking.setRoomId(room.getRoomId());
        booking.setGuestId(guestCreated.getGuestId());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCheckinDate(bookingRequestDTO.getCheckinDate());
        booking.setCheckoutDate(bookingRequestDTO.getCheckoutDate());
        booking.setNumGuests(bookingRequestDTO.getNumGuests());
        booking.setCreatedAt(LocalDateTime.now());

        roomServiceClient.updateRoomAvailability(room.getRoomId(), false);
        room.setAvailable(false);

        bookingRepository.save(booking);

        BookingResponseDTO newBooking = new BookingResponseDTO(
                booking.getBookingId(),
                guestResponse,
                room,
                booking.getCheckinDate(),
                booking.getCheckoutDate(),
                booking.getNumGuests(),
                "PENDING"
        );

        return newBooking;
    }

    @Override
    public BookingResponseDTO getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        Guest guest = guestRepository.findById(booking.getGuestId())
                .orElseThrow(() -> new GuestNotFoundException("Guest not found."));

        RoomResponseDto room = roomServiceClient.getRoomById(booking.getRoomId());

        return new BookingResponseDTO(
                booking.getBookingId(),
                modelMapper.map(guest, GuestResponseDTO.class),
                room,
                booking.getCheckinDate(),
                booking.getCheckoutDate(),
                booking.getNumGuests(),
                booking.getBookingStatus().name()
        );
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(booking -> {
            Guest guest = guestRepository.findById(booking.getGuestId())
                    .orElseThrow(() -> new GuestNotFoundException("Guest not found."));

            RoomResponseDto room = roomServiceClient.getRoomById(booking.getRoomId());

            return new BookingResponseDTO(
                    booking.getBookingId(),
                    modelMapper.map(guest, GuestResponseDTO.class),
                    room,
                    booking.getCheckinDate(),
                    booking.getCheckoutDate(),
                    booking.getNumGuests(),
                    booking.getBookingStatus().name()
            );
        }).collect(Collectors.toList());
    }

    public void updateBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        booking.setBookingStatus(BookingStatus.BOOKED); // Assuming "COMPLETED" after payment
        bookingRepository.save(booking);
    }


    @Override
    public BookingResponseDTO updateBooking(Long bookingId, BookingRequestDTO bookingRequestDTO) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        booking.setCheckinDate(bookingRequestDTO.getCheckinDate());
        booking.setCheckoutDate(bookingRequestDTO.getCheckoutDate());
        booking.setNumGuests(bookingRequestDTO.getNumGuests());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking updated = bookingRepository.save(booking);

        Guest guest = guestRepository.findById(booking.getGuestId())
                .orElseThrow(() -> new GuestNotFoundException("Guest not found."));

        RoomResponseDto room = roomServiceClient.getRoomById(updated.getRoomId());

        return new BookingResponseDTO(
                updated.getBookingId(),
                modelMapper.map(guest, GuestResponseDTO.class),
                room,
                updated.getCheckinDate(),
                updated.getCheckoutDate(),
                updated.getNumGuests(),
                updated.getBookingStatus().name()
        );
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));
        bookingRepository.delete(booking);
    }
}
