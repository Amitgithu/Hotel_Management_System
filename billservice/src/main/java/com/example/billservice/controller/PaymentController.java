package com.example.billservice.controller;

import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.imple.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillRepository billRepository;

    @GetMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam("sessionId") String sessionId) {
        return paymentService.handlePaymentSuccess(sessionId);
    }


    @GetMapping("/failed")
    public String paymentCancel() {
        return "Your payment was not successful. Please try again later.";
    }

}


