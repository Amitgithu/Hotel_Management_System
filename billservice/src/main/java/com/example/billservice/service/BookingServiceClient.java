package com.example.billservice.service;

import com.example.billservice.dto.BookingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "BOOKING-SERVICE", url = "http://localhost:8082/api/bookings")
public interface BookingServiceClient {

    @GetMapping("/{id}")
    BookingResponseDTO getBooking(@PathVariable("id") Long id);

    @PutMapping("/update-status/{bookingId}")
    void updateBookingStatus(@PathVariable Long bookingId);
}