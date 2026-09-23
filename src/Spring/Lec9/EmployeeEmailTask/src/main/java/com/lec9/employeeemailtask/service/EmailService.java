package com.lec9.employeeemailtask.service;

import com.lec9.employeeemailtask.dto.EmailResponse;

import java.util.List;

public interface EmailService {

    public EmailResponse createEmail(EmailResponse emailResponse);

    public EmailResponse updateEmail(EmailResponse emailResponse);

    public void deleteEmail(long id);

    public List<EmailResponse> getAllEmails();

    public List<EmailResponse> getEmailsByName(String name);

    public List<EmailResponse> getEmailsByListOfNames(List<String> names);

    public EmailResponse getEmailByContent(String content);

}
