package com.hotel.staffService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private Double salary;
    private String address;
    private Integer age;
    private String occupation;
    private String idProof;
    private String idProofNumber;
    private String phoneNumber;
    private LocalDate joiningDate;
    private String department;

}
