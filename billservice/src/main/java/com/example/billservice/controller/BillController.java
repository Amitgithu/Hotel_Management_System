
package com.example.billservice.controller;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/generate")
    public ResponseEntity<PaymentResponse> createBill(@RequestParam Long bookingId) throws Exception {
        return ResponseEntity.ok(billService.createBill(bookingId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBill(id));
    }

    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

}
