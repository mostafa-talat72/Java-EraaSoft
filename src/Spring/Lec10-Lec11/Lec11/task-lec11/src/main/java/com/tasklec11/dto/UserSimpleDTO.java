package com.tasklec11.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleDTO {

    private Long id;

    @Length(min = 8, message = "User name must be at least 8 characters")
    @NotBlank(message = "User name is required")
    @NotNull(message = "User name is required")
    private String name;

    @Min(value = 18, message = "User age must be at least 18")
    @NotNull(message = "User age is required")
    private Integer age;


    @NotNull(message = "User password is required")
    @NotBlank(message = "User password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;
}
