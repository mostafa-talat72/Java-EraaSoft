package com.restauranttablereservationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Full reservation view with the nested rows the client needs.
// status, createdAt, expiresAt, confirmedAt and cancelledAt are stored on the
// entity and kept current by the service.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;

    private RestaurantTableDTO table;

    private CustomerDTO customer;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer guests;

    private ReservationStatusDTO status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    private LocalDateTime cancelledAt;
}
