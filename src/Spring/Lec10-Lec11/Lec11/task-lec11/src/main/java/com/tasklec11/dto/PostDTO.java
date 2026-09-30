package com.tasklec11.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;

    @NotNull(message = "Post text is required")
    @NotBlank(message = "Post text is required")
    @Length(min = 20, message = "Post text must be at least 20 characters")
    private String text;

    private String imagePath;

    private UserSimpleDTO user;

}
