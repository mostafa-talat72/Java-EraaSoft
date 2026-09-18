package com.task1springrest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(unique = true, nullable = true)
    private String name;

    @Positive(message = "Number must be positive")
    @NotNull(message = "Number is required")
    @Column(name = "plyer_number")
    private Integer number;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary must be >= 0")
    private Double salary;

    public Player(String name, Integer number, Double salary) {
        this.name = name;
        this.number = number;
        this.salary = salary;
    }


    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", salary=" + salary +
                '}';
    }
}
