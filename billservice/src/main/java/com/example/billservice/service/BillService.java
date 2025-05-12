
package com.example.billservice.service;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface BillService {
    PaymentResponse createBill(@PathVariable Long bookingId) throws Exception;
    BillResponseDTO getBill(Long id);
    List<BillResponseDTO> getAllBills();
}
