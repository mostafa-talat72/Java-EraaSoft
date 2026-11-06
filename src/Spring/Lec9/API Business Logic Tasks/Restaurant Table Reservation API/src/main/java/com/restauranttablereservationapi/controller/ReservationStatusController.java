package com.restauranttablereservationapi.controller;

import com.restauranttablereservationapi.dto.ReservationStatusDTO;
import com.restauranttablereservationapi.dto.ReservationStatusRequestDTO;
import com.restauranttablereservationapi.service.ReservationStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservation-statuses")
@RequiredArgsConstructor
public class ReservationStatusController {

    private final ReservationStatusService reservationStatusService;

    @GetMapping
    public ResponseEntity<List<ReservationStatusDTO>> getAllReservationStatuses() {
        return ResponseEntity.ok(reservationStatusService.getAllReservationStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationStatusDTO> getReservationStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationStatusService.getReservationStatusById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationStatusDTO> createReservationStatus(
            @Valid @RequestBody ReservationStatusRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationStatusService.createReservationStatus(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationStatusDTO> updateReservationStatus(
            @PathVariable Long id,
            @Valid @RequestBody ReservationStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(reservationStatusService.updateReservationStatus(id, requestDTO));
    }

    // 204 on success; refused once the status is assigned to reservations.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationStatus(@PathVariable Long id) {
        reservationStatusService.deleteReservationStatus(id);
        return ResponseEntity.noContent().build();
    }
}
