package com.hotel.bookingService.service;

import com.hotel.bookingService.dto.BookingRequestDTO;
import com.hotel.bookingService.dto.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(String roomType, BookingRequestDTO bookingRequestDTO);

    BookingResponseDTO getBookingById(Long bookingId);

    List<BookingResponseDTO> getAllBookings();

    BookingResponseDTO updateBooking(Long bookingId, BookingRequestDTO bookingRequestDTO);

    void deleteBooking(Long bookingId);
    void updateBookingStatus(Long bookingId);
}

