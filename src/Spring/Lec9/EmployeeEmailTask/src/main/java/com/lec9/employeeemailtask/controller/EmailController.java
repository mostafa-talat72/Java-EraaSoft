package com.lec9.employeeemailtask.controller;

import com.lec9.employeeemailtask.dto.EmailResponse;
import com.lec9.employeeemailtask.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/emails")
// Email REST API: CRUD + lookups by name/content. All writes validated via @Valid.
public class EmailController {

    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // POST /emails -> 201 Created + Location header.
    @PostMapping
    public ResponseEntity<EmailResponse> createEmail(
            @Valid @RequestBody EmailResponse emailResponse) {

        EmailResponse response = emailService.createEmail(emailResponse);

        // Return 201 Created with the created Email
        return ResponseEntity
                .created(URI.create("/emails/" + response.getId()))
                .body(response);
    }

    // PUT /emails (id in body) -> 200 OK.
    @PutMapping
    public ResponseEntity<EmailResponse> updateEmail(
            @Valid @RequestBody EmailResponse emailResponse) {

        EmailResponse response = emailService.updateEmail(emailResponse);

        // Return 200 OK with the updated Email
        return ResponseEntity.ok(response);
    }

    // DELETE /emails/{id} -> 204 No Content.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(
            @PathVariable long id) {

        emailService.deleteEmail(id);

        // Return 204 No Content
        return ResponseEntity.noContent().build();
    }

    // GET /emails -> 200 OK list.
    @GetMapping
    public ResponseEntity<List<EmailResponse>> getAllEmails() {

        List<EmailResponse> emailResponseList =
                emailService.getAllEmails();

        // Return 200 OK with the Emails
        return ResponseEntity.ok(emailResponseList);
    }

    // GET /emails/{name} e.g. /emails/gmail -> 200 OK.
    @GetMapping("/{name}")
    public ResponseEntity<List<EmailResponse>> getEmailsByName(
            @PathVariable String name) {

        List<EmailResponse> emailResponseList =
                emailService.getEmailsByName(name);

        // Return 200 OK with the Emails
        return ResponseEntity.ok(emailResponseList);
    }

    // GET /emails/by-names?names=gmail,yahoo -> 200 OK.
    @GetMapping("/by-names")
    public ResponseEntity<List<EmailResponse>> getEmailsByListOfNames(
            @RequestParam List<String> names) {

        List<EmailResponse> emailResponseList =
                emailService.getEmailsByListOfNames(names);

        // Return 200 OK with the Emails
        return ResponseEntity.ok(emailResponseList);
    }

    // GET /emails/by-content/{content} -> 200 OK single.
    @GetMapping("/by-content/{content}")
    public ResponseEntity<EmailResponse> getEmailByContent(
            @PathVariable String content) {

        EmailResponse emailResponse =
                emailService.getEmailByContent(content);

        // Return 200 OK with the Email
        return ResponseEntity.ok(emailResponse);
    }
}