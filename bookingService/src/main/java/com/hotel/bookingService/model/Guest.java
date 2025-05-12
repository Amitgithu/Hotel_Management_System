package com.hotel.bookingService.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String address;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private IdProofType idProofType;

    @Column(nullable = false)
    private String idProofNumber;

    @CreationTimestamp
    private Instant createdAt;

    public enum IdProofType {
        AADHAR,
        PASSPORT,
        DRIVING_LICENSE,
        OTHER
    }
}
