package com.libraryapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// The test profile swaps Oracle for H2, so this runs without any database setup.
@SpringBootTest
@ActiveProfiles("test")
class LibraryBorrowingApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
