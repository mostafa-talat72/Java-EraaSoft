package com.appointmentbookingapi.controller;

import com.appointmentbookingapi.dto.MedicalServiceDTO;
import com.appointmentbookingapi.dto.MedicalServiceRequestDTO;
import com.appointmentbookingapi.service.MedicalServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Medical services (consultations, surgeries...) — the entity that defines
// each appointment's duration (rule 3 source).
@RestController
@RequestMapping("/medical-services")
@RequiredArgsConstructor
public class MedicalServiceController {

    private final MedicalServiceService medicalServiceService;

    @GetMapping
    public ResponseEntity<List<MedicalServiceDTO>> getAllMedicalServices() {
        return ResponseEntity.ok(medicalServiceService.getAllMedicalServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalServiceDTO> getMedicalServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalServiceService.getMedicalServiceById(id));
    }

    // Lookup by exact name: /medical-services/by-name?name=General%20Checkup
    @GetMapping("/by-name")
    public ResponseEntity<MedicalServiceDTO> getMedicalServiceByName(@RequestParam String name) {
        return ResponseEntity.ok(medicalServiceService.getMedicalServiceByName(name));
    }

    @PostMapping
    public ResponseEntity<MedicalServiceDTO> createMedicalService(
            @Valid @RequestBody MedicalServiceRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(medicalServiceService.createMedicalService(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalService(@PathVariable Long id) {
        medicalServiceService.deleteMedicalService(id);
        return ResponseEntity.noContent().build();
    }
}
