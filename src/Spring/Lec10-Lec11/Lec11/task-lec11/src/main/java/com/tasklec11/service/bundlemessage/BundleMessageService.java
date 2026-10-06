package com.tasklec11.service.bundlemessage;

import com.tasklec11.helper.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BundleMessageService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getMessageAr(String code){
        return messageSource.getMessage(code,null, new Locale("ar"));
    }

    public String getMessageEn(String code){
        return messageSource.getMessage(code,null, new Locale("en"));
    }

    public ResponseMessage getMessage(String code){
        return new ResponseMessage(getMessageAr(code), getMessageEn(code));
    }
}
