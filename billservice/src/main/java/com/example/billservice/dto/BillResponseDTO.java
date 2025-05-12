package com.example.billservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {

    private Long billId;
    private Long bookingId;
    private String customerName;
    private String sessionId;
    private Double totalAmount;
    private String paymentMode;
    private String paymentStatus;
    private LocalDateTime paymentDate;

}
