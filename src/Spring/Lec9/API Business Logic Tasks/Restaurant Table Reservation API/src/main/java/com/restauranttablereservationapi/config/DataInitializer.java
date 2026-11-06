package com.restauranttablereservationapi.config;

import com.restauranttablereservationapi.model.ReservationStatus;
import com.restauranttablereservationapi.repo.ReservationStatusRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Seeds the reservation state lookup table the business rules depend on.
// Idempotent: an existing row with the same name is left untouched.
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ReservationStatusRepo reservationStatusRepo;

    @Override
    public void run(String... args) {
        seedStatus("PENDING");
        seedStatus("CONFIRMED");
        seedStatus("CANCELLED");
        seedStatus("EXPIRED");
    }

    private void seedStatus(String name) {
        if (reservationStatusRepo.existsByName(name)) {
            return;
        }

        ReservationStatus status = new ReservationStatus();
        status.setName(name);
        reservationStatusRepo.save(status);

        log.info("Seeded reservation status '{}'", name);
    }
}
