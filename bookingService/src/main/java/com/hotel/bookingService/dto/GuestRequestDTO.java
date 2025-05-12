package com.hotel.bookingService.dto;

import com.hotel.bookingService.model.Guest.IdProofType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequestDTO
{

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    private String phone;

    private String address;

    private String nationality;

    @NotNull(message = "ID Proof Type is mandatory")
    private IdProofType idProofType;

    @NotBlank(message = "ID Proof Number is mandatory")
    private String idProofNumber;

}
