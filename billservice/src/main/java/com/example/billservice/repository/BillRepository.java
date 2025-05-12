
package com.example.billservice.repository;

import com.example.billservice.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findBySessionId(String sessionId);

    Optional<Bill> findByBookingId(Long bookingId);
}
