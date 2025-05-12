package com.example.billservice.service.imple;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.BookingResponseDTO;
import com.example.billservice.dto.PaymentResponse;
import com.example.billservice.entity.Bill;
import com.example.billservice.exception.ResourceNotFoundException;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.BillService;
import com.example.billservice.service.BookingServiceClient;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Override
    public PaymentResponse createBill(Long bookingId) throws Exception {

        BookingResponseDTO booking = bookingServiceClient.getBooking(bookingId);

        // Check if a bill already exists for this booking
        Optional<Bill> existingBill = billRepository.findByBookingId(bookingId);

        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();

            if (bill.getPaymentStatus().equalsIgnoreCase("SUCCESS")) {
                log.warn("Bill is already paid for booking ID: " + bookingId);
                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setCustomerName(bill.getCustomerName());
                paymentResponse.setSessionId(bill.getSessionId());
                paymentResponse.setSessionUrl("Bill is already paid for booking ID: " + bookingId); // No need for a checkout URL
                return paymentResponse;
            }


            // If bill already has a payment session, return existing session info
            if (bill.getSessionId() != null) {
                log.warn("Returning existing payment session for booking ID: " + bookingId);

                Session existingSession = paymentService.retrieveSession(bill.getSessionId());

                // If session is expired or canceled, mark payment as FAILED
                if ("expired".equalsIgnoreCase(existingSession.getStatus()) ||
                        "canceled".equalsIgnoreCase(existingSession.getStatus())) {
                    bill.setPaymentStatus("FAILED");
                    billRepository.save(bill); // Save failed status
                    throw new Exception("Payment session expired. Please create a new payment.");
                }

                Session session = paymentService.createCheckoutSession(bill.getTotalAmount(), "INR", bill.getCustomerName(), booking.getGuest().getEmail());

                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setCustomerName(bill.getCustomerName());
                paymentResponse.setSessionId(bill.getSessionId());
                paymentResponse.setSessionUrl(session.getUrl());
                return paymentResponse;
            }
        }

        Bill bill = new Bill();
        bill.setBookingId(booking.getBookingId());
        bill.setCustomerName(booking.getGuest().getFullName());
        bill.setTotalAmount(booking.getRoom().getPricePerNight());
        bill.setPaymentMode("CARD");
        bill.setPaymentStatus("PENDING");
        bill.setPaymentDate(LocalDateTime.now());

        Session session = paymentService.createCheckoutSession(bill.getTotalAmount(), "INR", bill.getCustomerName(), booking.getGuest().getEmail());
        log.warn("Payment Session Created Successfully");
        bill.setSessionId(session.getId());

        billRepository.save(bill);
        log.warn("Bill Saved Successfully");
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCustomerName(booking.getGuest().getFullName());
        paymentResponse.setSessionId(session.getId());
        paymentResponse.setSessionUrl(session.getUrl());

        return paymentResponse;
    }

    @Override
    public BillResponseDTO getBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        return modelMapper.map(bill, BillResponseDTO.class);
    }

    @Override
    public List<BillResponseDTO> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(bill -> modelMapper.map(bill, BillResponseDTO.class))
                .collect(Collectors.toList());
    }
}
