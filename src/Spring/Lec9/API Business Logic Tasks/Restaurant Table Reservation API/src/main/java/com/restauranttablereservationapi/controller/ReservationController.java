package com.restauranttablereservationapi.controller;

import com.restauranttablereservationapi.dto.ReservationDTO;
import com.restauranttablereservationapi.dto.ReservationRequestDTO;
import com.restauranttablereservationapi.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // POST /reservations - creates a PENDING reservation; 201 with its window.
    // All business rules (overlap, capacity, hours, cap, advance) live in the
    // service.
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(
            @Valid @RequestBody ReservationRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.createReservation(requestDTO));
    }

    // PUT /reservations/{id}/confirm - only inside the 15-minute window.
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ReservationDTO> confirmReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }

    // PUT /reservations/{id}/cancel - only at least 1 hour before the start.
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }

    // GET /reservations - every reservation, ordered by start time.
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // GET /reservations/{id} - one reservation; 404 through the service.
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    // GET /reservations/customer/{customerId}
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(reservationService.getReservationsByCustomer(customerId));
    }

    // GET /reservations/table/{tableId}
    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByTable(
            @PathVariable Long tableId) {
        return ResponseEntity.ok(reservationService.getReservationsByTable(tableId));
    }

}
