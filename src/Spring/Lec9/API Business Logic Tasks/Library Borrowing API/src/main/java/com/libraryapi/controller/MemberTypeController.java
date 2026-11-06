package com.libraryapi.controller;

import com.libraryapi.dto.MemberTypeDTO;
import com.libraryapi.dto.MemberTypeRequestDTO;
import com.libraryapi.service.MemberTypeService;
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
@RequestMapping("/member-types")
@RequiredArgsConstructor
public class MemberTypeController {

    private final MemberTypeService memberTypeService;

    // GET /member-types - every type with its loan period and book cap.
    @GetMapping
    public ResponseEntity<List<MemberTypeDTO>> getAllMemberTypes() {
        return ResponseEntity.ok(memberTypeService.getAllMemberTypes());
    }

    // GET /member-types/{id} - one type; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<MemberTypeDTO> getMemberTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(memberTypeService.getMemberTypeById(id));
    }

    // POST /member-types - create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<MemberTypeDTO> createMemberType(
            @Valid @RequestBody MemberTypeRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberTypeService.createMemberType(requestDTO));
    }

    // PUT /member-types/{id} - update name, loan period and cap.
    @PutMapping("/{id}")
    public ResponseEntity<MemberTypeDTO> updateMemberType(
            @PathVariable Long id,
            @Valid @RequestBody MemberTypeRequestDTO requestDTO) {
        return ResponseEntity.ok(memberTypeService.updateMemberType(id, requestDTO));
    }

    // DELETE /member-types/{id} - 204 on success; refused while members use it.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberType(@PathVariable Long id) {
        memberTypeService.deleteMemberType(id);
        return ResponseEntity.noContent().build();
    }
}
