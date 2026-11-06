package com.libraryapi.controller;

import com.libraryapi.dto.MemberDTO;
import com.libraryapi.dto.MemberRequestDTO;
import com.libraryapi.service.MemberService;
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
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // GET /members - every member with the type that decides loan period and cap.
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // GET /members/{id} - one member; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // POST /members - create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.createMember(requestDTO));
    }

    // PUT /members/{id} - update name, email and membership type.
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long id,
                                                  @Valid @RequestBody MemberRequestDTO requestDTO) {
        return ResponseEntity.ok(memberService.updateMember(id, requestDTO));
    }

    // DELETE /members/{id} - 204 on success; refused once the member has borrow history.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
