package com.example.billservice.service.imple;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.example.billservice.config.RabbitMQConfig.QUEUE_NAME;

@Component
public class MessageConsumer {

    private final EmailSenderService emailService;

    // ✅ Constructor Injection (Better than Field Injection)
    public MessageConsumer(EmailSenderService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("📩 Received message from RabbitMQ: " + message);

        // ✅ Ensure valid message format
        String[] data = message.split("\\|");
        if (data.length < 8) { // Adjusted for payment success message structure
            System.err.println("❌ Invalid message format: " + message);
            return;
        }

        // Trimming extra spaces for safety
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }

        // Extracting values
        String messageType = data[0];
        String email = data[1];
        String bookingId = data[2];
        String guestName = data[3];
        String roomType = data[4];
        String numGuests = data[5];
        String checkinDate = data[6];
        String checkoutDate = data[7];
        String totalAmount = data[8];

        String subject;
        String body;

        if (messageType.equals("PAYMENT_SUCCESS")) { // ✅ Added payment success case
            subject = "✨ Payment Successful - Booking Confirmation ✨";
            body = "Dear " + guestName + ",\n\n" +
                    "We are pleased to inform you that your payment for Booking ID: " + bookingId + " has been successfully processed.\n\n" +
                    "🏨 Booking Details:\n" +
                    "🛏️ Room Type: " + roomType + "\n" +
                    "👥 Number of Guests: " + numGuests + "\n" +
                    "📅 Check-in Date: " + checkinDate + "\n" +
                    "📅 Check-out Date: " + checkoutDate + "\n" +
                    "💰 Total Amount: " + totalAmount + "\n\n" +
                    "✅ Your reservation is confirmed! Thank you for choosing our service. We look forward to welcoming you soon.\n\n" +
                    "Best regards,\n" +
                    "Your Booking Team";

        } else {
            System.err.println("⚠ Unknown message type: " + messageType);
            return;
        }

        sendEmailAndLog(email, subject, body);
    }

    // ✅ Utility method to send emails and log
    private void sendEmailAndLog(String email, String subject, String body) {
        emailService.sendEmail(email, subject, body);
        System.out.println("✅ Email sent to: " + email + " | Subject: " + subject);
    }
}