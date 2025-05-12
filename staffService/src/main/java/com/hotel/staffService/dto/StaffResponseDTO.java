package com.hotel.staffService.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffResponseDTO {

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
