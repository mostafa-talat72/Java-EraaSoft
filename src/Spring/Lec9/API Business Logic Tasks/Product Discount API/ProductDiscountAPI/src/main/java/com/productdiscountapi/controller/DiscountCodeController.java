package com.productdiscountapi.controller;

import com.productdiscountapi.dto.DiscountCodeDTO;
import com.productdiscountapi.dto.DiscountCodeRequestDTO;
import com.productdiscountapi.service.DiscountCodeService;
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
@RequestMapping("/discount-codes")
@RequiredArgsConstructor
public class DiscountCodeController {

    private final DiscountCodeService discountCodeService;

    // GET /discount-codes — list every code with its incompatible codes.
    @GetMapping
    public ResponseEntity<List<DiscountCodeDTO>> getAllDiscountCodes() {
        return ResponseEntity.ok(discountCodeService.getAllDiscountCodes());
    }

    // GET /discount-codes/{id} — one code by database id.
    @GetMapping("/{id}")
    public ResponseEntity<DiscountCodeDTO> getDiscountCodeById(@PathVariable Long id) {
        return ResponseEntity.ok(discountCodeService.getDiscountCodeById(id));
    }

    // GET /discount-codes/code/{code} — one code by its unique string
    // (e.g. /discount-codes/code/SAVE10).
    @GetMapping("/code/{code}")
    public ResponseEntity<DiscountCodeDTO> getDiscountCodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(discountCodeService.getDiscountCodeByCode(code));
    }

    // POST /discount-codes — create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<DiscountCodeDTO> createDiscountCode(
            @Valid @RequestBody DiscountCodeRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(discountCodeService.createDiscountCode(requestDTO));
    }

    // PUT /discount-codes/{id} — update an existing code.
    @PutMapping("/{id}")
    public ResponseEntity<DiscountCodeDTO> updateDiscountCode(
            @PathVariable Long id,
            @Valid @RequestBody DiscountCodeRequestDTO requestDTO) {
        return ResponseEntity.ok(discountCodeService.updateDiscountCode(id, requestDTO));
    }

    // DELETE /discount-codes/{id} — 204 with no body on success.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscountCode(@PathVariable Long id) {
        discountCodeService.deleteDiscountCode(id);
        return ResponseEntity.noContent().build();
    }
}
