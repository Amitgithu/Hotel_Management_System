package com.hotel.staffService.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffRequestDTO {

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Salary is mandatory")
    @Positive(message = "Salary must be positive")
    private Double salary;

    private String address;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 65, message = "Age must be less than or equal to 65")
    private Integer age;

    @NotBlank(message = "Occupation is mandatory")
    private String occupation;

    @NotBlank(message = "ID Proof is mandatory")
    private String idProof;

    @NotBlank(message = "ID Proof Number is mandatory")
    private String idProofNumber;

    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    private LocalDate joiningDate;

    private String department;
}
