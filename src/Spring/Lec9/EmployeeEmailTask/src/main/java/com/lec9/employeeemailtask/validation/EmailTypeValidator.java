package com.lec9.employeeemailtask.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class EmailTypeValidator
        implements ConstraintValidator<ValidEmailType, Object> {

    // Passes on null name/content so @NotBlank reports them; compares provider vs type otherwise.
    @Override
    public boolean isValid(
            Object object,
            ConstraintValidatorContext context) {

        if (object == null) {
            return true;
        }

        try {

            Field nameField = object.getClass().getDeclaredField("name");
            Field contentField = object.getClass().getDeclaredField("content");

            nameField.setAccessible(true);
            contentField.setAccessible(true);

            String name = (String) nameField.get(object);
            String content = (String) contentField.get(object);

            if (name == null || content == null) {
                return true;
            }

            String domain = content.substring(
                    content.lastIndexOf("@") + 1
            );

            String provider = domain.split("\\.")[0];

            // Mismatch (e.g. type=gmail but content=@yahoo.com) -> violation on content.
            if (!provider.equalsIgnoreCase(name)) {

                context.disableDefaultConstraintViolation();

                context.buildConstraintViolationWithTemplate(
                                "Email content must match email type"
                        )
                        .addPropertyNode("content")
                        .addConstraintViolation();

                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}