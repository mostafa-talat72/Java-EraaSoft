package com.tasklec8.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Instructor name must not be null")
    @NotBlank(message = "Instructor name must not be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Instructor email must not be null")
    @NotBlank(message = "Instructor email must not be blank")
    @Email(message = "Instructor email must be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses = new ArrayList<>();

    public Instructor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Instructor(long id, String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Instructor(String name, String email, List<Course> courses) {
        this.name = name;
        this.email = email;
        this.courses = courses;
    }
}