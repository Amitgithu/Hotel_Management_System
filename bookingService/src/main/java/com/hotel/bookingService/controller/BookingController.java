package com.hotel.bookingService.controller;

import com.hotel.bookingService.dto.BookingRequestDTO;
import com.hotel.bookingService.dto.BookingResponseDTO;
import com.hotel.bookingService.service.BookingService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @CircuitBreaker(name = "bookingService", fallbackMethod = "fallbackCreateBooking")
    public ResponseEntity<?> createBooking(
            @RequestParam String roomType,
            @Valid @RequestBody BookingRequestDTO bookingRequestDTO) {

        BookingResponseDTO savedBooking = bookingService.createBooking(roomType, bookingRequestDTO);
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    // Fallback method when circuit breaker is triggered
    public ResponseEntity<String> fallbackCreateBooking(String roomType, BookingRequestDTO bookingRequestDTO, FeignException t) {
        BookingResponseDTO fallbackResponse = new BookingResponseDTO();
        return ResponseEntity.status(503).body("Booking service is currently unavailable. Please try again later.");
    }


    @GetMapping("/{id}")
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackGetBooking")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        BookingResponseDTO bookingResponseDTO = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingResponseDTO);
    }

    // Fallback methods
    public ResponseEntity<String> fallbackGetBooking(Long id, FeignException t) {
        return ResponseEntity.status(503).body("Booking service is currently unavailable. Unable to retrieve booking details. Please try again later.");
    }

    @GetMapping
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackGetAllBookings")
    public ResponseEntity<List<?>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // Fallback methods
    public ResponseEntity<String> fallbackGetAllBookings(FeignException t) {
        return ResponseEntity.status(503).body("Booking service is currently unavailable. Please try again later.");
    }

    @PutMapping("/{id}")
    @CircuitBreaker(name = "bookingServiceCircuitBreaker", fallbackMethod = "fallbackUpdateBooking")
    public ResponseEntity<?> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingRequestDTO));
    }

    // Fallback method
    public ResponseEntity<String> fallbackUpdateBooking(Long id, BookingRequestDTO requestDTO, FeignException t) {
        return ResponseEntity.status(503).body("Booking service is currently unavailable. Unable to update booking at the moment. Please try again later.");
    }

    @PutMapping("/update-status/{bookingId}")
    public ResponseEntity<String> updateBookingStatus(@PathVariable Long bookingId) {
        try {
            bookingService.updateBookingStatus(bookingId);
            return ResponseEntity.ok("Booking status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating booking status: " + e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
