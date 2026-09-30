package com.tasklec11.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Post text is required")
    @NotBlank(message = "Post text is required")
    @Length(min = 20, message = "Post text must be at least 20 characters")
    @Column(
            nullable = false,
            check = @CheckConstraint(
                    name = "CH_TEXT",
                    constraint = "LENGTH(text) >= 20"
            )
    )
    private String text;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
