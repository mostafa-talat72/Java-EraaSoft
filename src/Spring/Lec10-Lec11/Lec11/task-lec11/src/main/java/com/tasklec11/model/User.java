package com.tasklec11.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(min = 8, message = "User name must be at least 8 characters")
    @NotBlank(message = "User name is required")
    @NotNull(message = "User name is required")
    @Column(
            nullable = false,
            check = {
                    @CheckConstraint(
                            name = "CH_NAME",
                            constraint = "LENGTH(name) > 7"
                    )
            }
    )
    private String name;

    @Min(value = 18, message = "User age must be at least 18")
    @NotNull(message = "User age is required")
    @Column(nullable = false)
    private Integer age;


    @NotNull(message = "User password is required")
    @NotBlank(message = "User password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    @Column(
            nullable = false,
            check = {
                    @CheckConstraint(
                            name = "CH_PASSWORD",
                            constraint = "REGEXP_LIKE(password, '[A-Z]') AND REGEXP_LIKE(password, '[a-z]') AND REGEXP_LIKE(password, '[0-9]') AND REGEXP_LIKE(password, '[@#$%^&+=!]')"
                    )
            }
    )
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
}
