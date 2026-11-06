package com.libraryapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Typed view of the "library.*" block in application.yaml.
// The fine rate is a policy value, so it stays out of the code.
@Component
@ConfigurationProperties(prefix = "library")
@Getter
@Setter
public class LibraryProperties {

    // Fine charged for every day a book is returned after its due date (rules 5 and 6).
    private BigDecimal finePerDay = new BigDecimal("5.00");
}
