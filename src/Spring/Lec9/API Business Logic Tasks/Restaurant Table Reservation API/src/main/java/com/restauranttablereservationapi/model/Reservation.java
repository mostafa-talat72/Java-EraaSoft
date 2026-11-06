package com.restauranttablereservationapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "RT_RESERVATION", indexes = {
        // rule 1: overlap check on a table for a time window
        @Index(name = "IDX_RT_RES_TABLE_START", columnList = "table_id, start_time"),
        // expiry job: pending rows whose deadline passed
        @Index(name = "IDX_RT_RES_STATUS_EXPIRES", columnList = "status_id, expires_at"),
        // rule 4: count active reservations of a customer
        @Index(name = "IDX_RT_RES_CUSTOMER_STATUS", columnList = "customer_id, status_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Table is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // startTime + the durationMinutes sent by the client.
    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @NotNull(message = "Guests is required")
    @Min(value = 1, message = "At least one guest is required")
    @Column(nullable = false)
    private Integer guests;

    // Stored state: PENDING -> CONFIRMED / CANCELLED / EXPIRED.
    @NotNull(message = "Reservation status is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private ReservationStatus status;

    // Set by the service from the injected Clock, never by @PrePersist:
    // a fixed Clock in tests must control every timestamp.
    @NotNull(message = "Created at is required")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // createdAt + confirm-window-minutes (rule 7).
    @NotNull(message = "Expires at is required")
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    // null until the reservation is confirmed.
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    // null until the reservation is cancelled.
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
}
