package com.banktransferapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Uses the test profile so the context starts against H2 instead of a local Oracle.
@SpringBootTest
@ActiveProfiles("test")
class BankTransferApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
