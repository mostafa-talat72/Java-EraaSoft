package com.banktransferapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for PUT /accounts/{id}.
// accountNumber and balance are intentionally absent: neither can be changed
// directly here, the balance only moves through transfers.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequestDTO {

    @NotBlank(message = "Holder name is required")
    @Size(max = 100, message = "Holder name cannot exceed 100 characters")
    private String holderName;

    @NotNull(message = "Account type id is required")
    private Long accountTypeId;

    @NotNull(message = "Active flag is required")
    private Boolean active;
}
