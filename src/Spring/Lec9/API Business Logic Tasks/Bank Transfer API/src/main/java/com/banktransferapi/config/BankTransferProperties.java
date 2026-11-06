package com.banktransferapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Typed view of the "bank.transfer.*" block in application.yaml.
// A dedicated class keeps the service free of hard-coded thresholds and
// makes every value overridable per environment.
@Component
@ConfigurationProperties(prefix = "bank.transfer")
@Getter
@Setter
public class BankTransferProperties {

    // Transfers STRICTLY above this amount need a verification code (rule 5).
    private BigDecimal verificationThreshold = new BigDecimal("10000");
}
