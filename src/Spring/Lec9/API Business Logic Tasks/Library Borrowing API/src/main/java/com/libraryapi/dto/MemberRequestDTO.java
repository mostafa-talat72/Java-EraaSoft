package com.libraryapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST /members and PUT /members/{id}.
// typeId points at the member-type row that drives the loan period and the cap.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {

    @NotBlank(message = "Member name is required")
    @Size(max = 100, message = "Member name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Member email is required")
    @Email(message = "Member email must be valid")
    @Size(max = 100, message = "Member email cannot exceed 100 characters")
    private String email;

    @NotNull(message = "Member type id is required")
    private Long typeId;
}
