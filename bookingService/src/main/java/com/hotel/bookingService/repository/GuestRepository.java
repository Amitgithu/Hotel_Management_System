package com.hotel.bookingService.repository;


import com.hotel.bookingService.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}

