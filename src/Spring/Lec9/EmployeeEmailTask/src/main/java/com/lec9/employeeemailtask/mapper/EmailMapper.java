package com.lec9.employeeemailtask.mapper;


import com.lec9.employeeemailtask.dto.EmailResponse;
import com.lec9.employeeemailtask.dto.EmailSimpleResponse;
import com.lec9.employeeemailtask.model.Email;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    // Full and slim conversions both ways; employee link carried by id reference.
    Email convertFromEmailResponseToEmail(EmailResponse emailResponse);

    EmailResponse convertFromEmailToEmailResponse(Email email);

    List<Email> convertFromEmailResponseListToEmailList(List<EmailResponse> emailResponseList);

    List<EmailResponse> convertFromEmailListToEmailResponseList(List<Email> emails);

    Email convertFromEmailSimpleResponseToEmail(EmailSimpleResponse emailSimpleResponse);

    EmailSimpleResponse convertFromEmailToEmailSimpleResponse(Email email);

    List<Email> convertFromEmailSimpleResponseListToEmailList(List<EmailSimpleResponse> emailSimpleResponseList);

    List<EmailSimpleResponse> convertFromEmailListToEmailSimpleResponseList(List<Email> emails);
}
