package com.appointmentbookingapi.controller;

import com.appointmentbookingapi.dto.DoctorUnavailabilityDTO;
import com.appointmentbookingapi.dto.DoctorUnavailabilityRequestDTO;
import com.appointmentbookingapi.service.DoctorUnavailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Blocked time ranges per doctor (leaves, sick hours, ...).
// Creating one does NOT touch existing BOOKED appointments (agreed decision B):
// rule 4 is enforced only when new bookings arrive.
@RestController
@RequestMapping("/unavailabilities")
@RequiredArgsConstructor
public class DoctorUnavailabilityController {

    private final DoctorUnavailabilityService doctorUnavailabilityService;

    @GetMapping
    public ResponseEntity<List<DoctorUnavailabilityDTO>> getAllDoctorUnavailabilities() {
        return ResponseEntity.ok(doctorUnavailabilityService.getAllDoctorUnavailabilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorUnavailabilityDTO> getDoctorUnavailabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorUnavailabilityService.getDoctorUnavailabilityById(id));
    }

    @PostMapping
    public ResponseEntity<DoctorUnavailabilityDTO> createDoctorUnavailability(
            @Valid @RequestBody DoctorUnavailabilityRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorUnavailabilityService.createDoctorUnavailability(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorUnavailability(@PathVariable Long id) {
        doctorUnavailabilityService.deleteDoctorUnavailability(id);
        return ResponseEntity.noContent().build();
    }
}
