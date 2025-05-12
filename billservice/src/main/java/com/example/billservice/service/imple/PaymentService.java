package com.example.billservice.service.imple;

import com.example.billservice.dto.BookingResponseDTO;
import com.example.billservice.entity.Bill;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.service.BookingServiceClient;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Autowired
    EmailSenderService emailSenderService;  // EmailSenderService is used to send emails.

    @Autowired
    MessageProducer messageProducer;        // EventPublisherService is used to publish events.

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public Session createCheckoutSession(@NotNull Double amount, String currency, String customerName, String email) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8091/api/payment/success?sessionId={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8091/api/payment/failed?sessionId={CHECKOUT_SESSION_ID}")
                .setCustomerEmail(email)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount((long) (amount * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(customerName)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .setCustomerCreation(SessionCreateParams.CustomerCreation.ALWAYS)
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                                .build()
                )
                .build();

        Session session = Session.create(params);
        log.info("Created Stripe Session: {}", session);
        log.info("Created Stripe Session ID: {}", session.getId());
        log.info("Created Stripe Payment URL: {}", session.getUrl());
        return session;
    }

    public Session retrieveSession(String sessionId) throws Exception {
        Session session = Session.retrieve(sessionId);
        log.info(String.valueOf(session));
        return session;
    }

    public ResponseEntity<String> handlePaymentSuccess(String sessionId) {
        try {
            // 1. Retrieve the session from Stripe
            Session session = retrieveSession(sessionId);

            // 2. Check the payment status
            if ("paid".equalsIgnoreCase(session.getPaymentStatus())) {
                // 3. Update the bill in the database
                Optional<Bill> billOptional  = billRepository.findBySessionId(sessionId);

                if (billOptional.isPresent()) {
                    Bill bill = billOptional.get();
                    bill.setPaymentStatus("SUCCESS");
                    bill.setPaymentDate(LocalDateTime.now());
                    billRepository.save(bill);

                    // 4. Retrieve booking details
                    BookingResponseDTO bookingDetails = bookingServiceClient.getBooking(bill.getBookingId());

                    String customMessage = "PAYMENT_SUCCESS|" + bookingDetails.getGuest().getEmail() + "|" + bill.getBookingId() + "|" +
                            bookingDetails.getGuest().getFullName() + "|" +
                            bookingDetails.getRoom().getRoomType() + "|" +
                            bookingDetails.getNumGuests() + "|" +
                            bookingDetails.getCheckinDate() + "|" +
                            bookingDetails.getCheckoutDate() + "|" +
                            bill.getTotalAmount() + " " + session.getCurrency();

                    messageProducer.sendMessage(customMessage);

                    // 7. Update booking status
                    bookingServiceClient.updateBookingStatus(bill.getBookingId());

                    return ResponseEntity.ok("Payment successful. Bill status updated.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Bill not found for session ID: " + sessionId);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment not completed for session ID: " + sessionId);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment success: " + e.getMessage());
        }
    }
}