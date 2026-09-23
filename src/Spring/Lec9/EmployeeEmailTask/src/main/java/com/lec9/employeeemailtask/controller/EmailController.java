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
public class EmailController {

    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Create a new Email.
     *
     * POST /emails
     */
    @PostMapping
    public ResponseEntity<EmailResponse> createEmail(
            @Valid @RequestBody EmailResponse emailResponse) {

        // Create the Email
        EmailResponse response = emailService.createEmail(emailResponse);

        // Return 201 Created with the created Email
        return ResponseEntity
                .created(URI.create("/emails/" + response.getId()))
                .body(response);
    }

    /**
     * Update an existing Email.
     *
     * PUT /emails
     */
    @PutMapping
    public ResponseEntity<EmailResponse> updateEmail(
            @Valid @RequestBody EmailResponse emailResponse) {

        // Update the Email
        EmailResponse response = emailService.updateEmail(emailResponse);

        // Return 200 OK with the updated Email
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an Email by ID.
     *
     * DELETE /emails/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(
            @PathVariable long id) {

        // Delete the Email
        emailService.deleteEmail(id);

        // Return 204 No Content
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all Emails.
     *
     * GET /emails
     */
    @GetMapping
    public ResponseEntity<List<EmailResponse>> getAllEmails() {

        // Retrieve all Emails
        List<EmailResponse> emailResponseList =
                emailService.getAllEmails();

        // Return 200 OK with the Emails
        return ResponseEntity.ok(emailResponseList);
    }

    /**
     * Get Emails by Email type/name.
     *
     * Example:
     * GET /emails/gmail
     */
    @GetMapping("/{name}")
    public ResponseEntity<List<EmailResponse>> getEmailsByName(
            @PathVariable String name) {

        // Retrieve Emails by their type/name
        List<EmailResponse> emailResponseList =
                emailService.getEmailsByName(name);

        // Return 200 OK with the Emails
        return ResponseEntity.ok(emailResponseList);
    }

    /**
     * Get Emails by a list of Email types/names.
     *
     * Example:
     * GET /emails/by-names?names=gmail,yahoo,hotmail
     */
    @GetMapping("/by-names")
    public ResponseEntity<List<EmailResponse>> getEmailsByListOfNames(
            @RequestParam List<String> names) {

        // Retrieve Emails whose names match the provided names
        List<EmailResponse> emailResponseList =
                emailService.getEmailsByListOfNames(names);

        // Return 200 OK with the Emails
        return ResponseEntity.ok(emailResponseList);
    }

    /**
     * Get an Email by its actual email content.
     *
     * Example:
     * GET /emails/by-content/ahmed@gmail.com
     */
    @GetMapping("/by-content/{content}")
    public ResponseEntity<EmailResponse> getEmailByContent(
            @PathVariable String content) {

        // Retrieve the Email by its content
        EmailResponse emailResponse =
                emailService.getEmailByContent(content);

        // Return 200 OK with the Email
        return ResponseEntity.ok(emailResponse);
    }
}