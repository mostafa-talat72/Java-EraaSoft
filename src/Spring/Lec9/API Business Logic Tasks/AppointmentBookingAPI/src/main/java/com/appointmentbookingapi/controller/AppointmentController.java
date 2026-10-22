package com.appointmentbookingapi.controller;

import com.appointmentbookingapi.dto.AppointmentDTO;
import com.appointmentbookingapi.dto.AppointmentRequestDTO;
import com.appointmentbookingapi.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// The core of the API: booking, cancellation, and lookups.
// Every business rule (1..6) lives in AppointmentService; this class only
// parses HTTP input and shapes the response/status codes.
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Rule 3: the client sends ONLY the start time — endTime is computed
    // from the chosen medical service and never accepted from the body.
    // Success -> 201 + the fully mapped AppointmentDTO.
    // Failures -> 400 from the rules (4/1/2) or 404 for unknown ids.
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(requestDTO));
    }

    // Cancel: 200 with the updated appointment (status -> CANCELED),
    // 400 if rules 5/6 reject (late cancellation / already canceled),
    // 404 for an unknown id.
    @PostMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    // {id} is narrowed to digits (\\d+) so literal sibling paths like
    // /doctor-appointments can never be swallowed by this pattern.
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // Doctor's BOOKED appointments for one day as a proper GET:
    // /appointments/doctor-appointments?doctorId=2000&date=2026-11-10
    // (GET must not carry a body — the day is a query param, ISO yyyy-MM-dd.)
    // An empty day returns 200 with [] (normal state, not an error).
    @GetMapping("/doctor-appointments")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId, date));
    }
}
