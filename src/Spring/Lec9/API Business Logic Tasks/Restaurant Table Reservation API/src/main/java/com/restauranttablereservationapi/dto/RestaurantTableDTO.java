package com.restauranttablereservationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableDTO {

    private Long id;

    private String tableNumber;

    private Integer capacity;

    private boolean active;
}
