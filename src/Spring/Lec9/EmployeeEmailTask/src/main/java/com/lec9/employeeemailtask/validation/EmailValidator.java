package com.lec9.employeeemailtask.validation;

import com.lec9.employeeemailtask.dto.EmailSimpleResponse;
import com.lec9.employeeemailtask.exception.EmailException;
import com.lec9.employeeemailtask.repo.EmailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EmailValidator {

    private EmailRepo emailRepo;

    @Autowired
    public EmailValidator(EmailRepo emailRepo) {
        this.emailRepo = emailRepo;
    }

    public void checkContentForCreate(List<EmailSimpleResponse> emails) {
        Set<String> idErrors = emails.stream()
                .filter(email -> Objects.nonNull(email.getId()))
                .map(email -> email.getId().toString())
                .collect(Collectors.toSet());
        if (!idErrors.isEmpty()) {
            throw new EmailException("id", "Id must be null in request for ids: " + String.join(", ", idErrors));
        }

        Set<String> contentDistinct = new HashSet<>();
        Set<String> errors =  emails.stream()
                .filter(email->  !contentDistinct.add(email.getContent()))
                .map(email -> email.getContent())
                .collect(Collectors.toSet());
        if (!errors.isEmpty()) {
            throw new EmailException("content", "Duplicate email in request: " + String.join(", ", errors));
        }

         errors = contentDistinct.stream()
                .filter(content -> emailRepo.existsByContent(content))
                .map(content -> content).collect(Collectors.toSet());

        if (!errors.isEmpty()) {
            throw new EmailException("content", "This emails already exists: " + String.join(", ", errors));
        }


    }

    public void checkContentForUpdate(List<EmailSimpleResponse> emails) {
        Set<String> idErrors = emails.stream()
                .filter(email -> Objects.isNull(email.getId()))
                .map(email -> email.getContent())
                .collect(Collectors.toSet());
        if (!idErrors.isEmpty()) {
            throw new EmailException("id", "Id must be not null in request for content: " + String.join(", ", idErrors));
        }

        Set<Long> idsDistinct = new HashSet<>();
        idErrors = emails.stream()
                .filter(email -> !idsDistinct.add(email.getId()))
                .map(email -> email.getId().toString())
                .collect(Collectors.toSet());
        if (!idErrors.isEmpty()) {
            throw new EmailException("id", "Duplicate id in request: " + String.join(", ", idErrors));
        }

        idErrors = emails.stream()
                .filter(email -> !emailRepo.existsById(email.getId()))
                .map(email -> email.getId().toString())
                .collect(Collectors.toSet());

        if (!idErrors.isEmpty()) {
            throw new EmailException("id", "This emails ids dose not exists: " + String.join(", ", idErrors));
        }

        Set<String> contentDistinct = new HashSet<>();
        Set<String> errors =  emails.stream()
                .filter(email->  !contentDistinct.add(email.getContent()))
                .map(email -> email.getContent())
                .collect(Collectors.toSet());
        if (!errors.isEmpty()) {
            throw new EmailException("content", "Duplicate email in request: " + String.join(", ", errors));
        }

        errors = emails.stream()
                .filter(email -> emailRepo.existsByContentAndIdNot(email.getContent(), email.getId()))
                .map(email -> email.getContent()).collect(Collectors.toSet());

        if (!errors.isEmpty()) {
            throw new EmailException("content", "This emails already exists: " + String.join(", ", errors));
        }

    }
}
