package com.appointmentbookingapi.controller;

import com.appointmentbookingapi.dto.AppointmentStatusDTO;
import com.appointmentbookingapi.service.AppointmentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Status dictionary (BOOKED / CANCELED) — read-only: rows are seeded at startup,
// so there is intentionally NO create/delete endpoint here.
@RestController
@RequestMapping("/appointment-statuses")
@RequiredArgsConstructor
public class AppointmentStatusController {

    private final AppointmentStatusService appointmentStatusService;

    @GetMapping
    public ResponseEntity<List<AppointmentStatusDTO>> getAllAppointmentStatuses() {
        return ResponseEntity.ok(appointmentStatusService.getAllAppointmentStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentStatusDTO> getAppointmentStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentStatusService.getAppointmentStatusById(id));
    }

    // /appointment-statuses/by-name?name=BOOKED
    @GetMapping("/by-name")
    public ResponseEntity<AppointmentStatusDTO> getAppointmentStatusByName(@RequestParam String name) {
        return ResponseEntity.ok(appointmentStatusService.getAppointmentStatusByName(name));
    }
}
