package com.libraryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibraryBorrowingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryBorrowingApiApplication.class, args);
    }

}
