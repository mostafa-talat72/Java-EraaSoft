package com.lec9.employeeemailtask.service.impl;

import com.lec9.employeeemailtask.dto.EmailResponse;
import com.lec9.employeeemailtask.dto.EmployeeSimpleResponse;
import com.lec9.employeeemailtask.exception.EmailException;
import com.lec9.employeeemailtask.mapper.EmailMapper;
import com.lec9.employeeemailtask.model.Email;
import com.lec9.employeeemailtask.repo.EmailRepo;
import com.lec9.employeeemailtask.service.EmailService;
import com.lec9.employeeemailtask.validation.EmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private EmailRepo emailRepo;

    private EmailMapper emailMapper;

    private EmployeeValidator employeeValidator;

    @Autowired
    public EmailServiceImpl(EmailRepo emailRepo, EmailMapper emailMapper,  EmployeeValidator employeeValidator) {
        this.emailRepo = emailRepo;
        this.emailMapper = emailMapper;
        this.employeeValidator = employeeValidator;
    }

    /**
     * Create a new Email.
     *
     * The ID must be null because it will be generated automatically.
     * The email content must also be unique.
     */
    @Override
    public EmailResponse createEmail(EmailResponse emailResponse) {

        // The ID must not be provided when creating a new Email
        if (Objects.nonNull(emailResponse.getId())) {
            throw new EmailException(
                    "id",
                    "Email ID must be null when creating a new email"
            );
        }

        // Check that the email content is not already registered
        if (emailRepo.existsByContent(emailResponse.getContent())) {
            throw new EmailException(
                    "content",
                    "This email already exists: " + emailResponse.getContent()
            );
        }

        EmployeeSimpleResponse employeeSimpleResponse = employeeValidator.checkEmployee(emailResponse.getEmployee());

        emailResponse.setEmployee(employeeSimpleResponse);
        // Convert DTO to Entity
        Email email =
                emailMapper.convertFromEmailResponseToEmail(emailResponse);

        // Save the Email
        email = emailRepo.save(email);

        // Convert the saved Entity back to DTO
        return emailMapper.convertFromEmailToEmailResponse(email);
    }

    /**
     * Update an existing Email.
     *
     * The Email must exist and its content must remain unique.
     */
    @Override
    public EmailResponse updateEmail(EmailResponse emailResponse) {

        // The ID is required when updating an Email
        if (Objects.isNull(emailResponse.getId())) {
            throw new EmailException(
                    "id",
                    "Email ID must not be null when updating an email"
            );
        }

        // Check that the Email being updated actually exists
        if (!emailRepo.existsById(emailResponse.getId())) {
            throw new EmailException(
                    "id",
                    "Email does not exist with ID: " + emailResponse.getId()
            );
        }

        // Check that the new email content is not used by another Email
        if (emailRepo.existsByContentAndIdNot(
                emailResponse.getContent(),
                emailResponse.getId())) {

            throw new EmailException(
                    "content",
                    "This email already exists: " + emailResponse.getContent()
            );
        }

        EmployeeSimpleResponse employeeSimpleResponse = employeeValidator.checkEmployee(emailResponse.getEmployee());

        emailResponse.setEmployee(employeeSimpleResponse);

        // Convert DTO to Entity
        Email email =
                emailMapper.convertFromEmailResponseToEmail(emailResponse);

        // Save the updated Email
        email = emailRepo.save(email);

        // Convert the updated Entity back to DTO
        return emailMapper.convertFromEmailToEmailResponse(email);
    }

    /**
     * Delete an Email by ID.
     *
     * The Email must exist before deleting it.
     */
    @Override
    public void deleteEmail(long id) {

        // Check that the Email exists
        if (!emailRepo.existsById(id)) {
            throw new EmailException(
                    "id",
                    "Email does not exist with ID: " + id
            );
        }

        // Delete the Email
        emailRepo.deleteById(id);
    }

    /**
     * Get all Emails.
     *
     * Throws an exception if no Emails exist.
     */
    @Override
    public List<EmailResponse> getAllEmails() {

        // Retrieve all Emails from the database
        List<Email> emails = emailRepo.findAll();

        // Check if there are any Emails
        if (emails.isEmpty()) {
            throw new EmailException(
                    "Emails",
                    "No emails found"
            );
        }

        // Convert Entity list to DTO list
        return emailMapper.convertFromEmailListToEmailResponseList(emails);
    }

    /**
     * Get Emails by Email type/name.
     *
     * Example:
     * name = gmail
     */
    @Override
    public List<EmailResponse> getEmailsByName(String name) {

        // Find all Emails with the specified type/name
        List<Email> emails = emailRepo.findByNameIgnoreCase(name);

        // Check if any Emails were found
        if (emails.isEmpty()) {
            throw new EmailException(
                    "name",
                    "No emails found with name: " + name
            );
        }

        // Convert Entity list to DTO list
        return emailMapper.convertFromEmailListToEmailResponseList(emails);
    }

    /**
     * Get Emails by a list of Email types/names.
     *
     * The method also checks that all requested names exist.
     */
    @Override
    public List<EmailResponse> getEmailsByListOfNames(List<String> names) {

        // Find all Emails whose names match the provided names
        List<Email> emails = emailRepo.findAllByNameInIgnoreCase(names);

        // Extract the names that were actually found
        Set<String> foundNames = emails.stream()
                .map(email -> email.getName())
                .collect(Collectors.toSet());

        // Find the requested names that were not found
        List<String> missingNames = names.stream()
                .distinct()
                .filter(name -> !foundNames.contains(name))
                .toList();

        // Throw an exception if any requested name does not exist
        if (!missingNames.isEmpty()) {
            throw new EmailException(
                    "Emails",
                    "Emails with the following names were not found: "
                            + missingNames
            );
        }

        // Convert Entity list to DTO list
        return emailMapper.convertFromEmailListToEmailResponseList(emails);
    }

    /**
     * Get an Email by its actual email content.
     *
     * Example:
     * ahmed@gmail.com
     */
    @Override
    public EmailResponse getEmailByContent(String content) {

        // Find the Email by its content
        Email email = emailRepo.findByContent(content);

        // Check if the Email exists
        if (Objects.isNull(email)) {
            throw new EmailException(
                    "content",
                    "Email does not exist with content: " + content
            );
        }

        // Convert Entity to DTO
        return emailMapper.convertFromEmailToEmailResponse(email);
    }
}