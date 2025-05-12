
package com.example.billservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long bookingId;

    @NotBlank
    private String customerName;

    @NotNull
    private Double totalAmount;

    @NotBlank
    private String paymentMode;

    @NotBlank
    private String paymentStatus;

    private LocalDateTime paymentDate;

    private String sessionId;
}
