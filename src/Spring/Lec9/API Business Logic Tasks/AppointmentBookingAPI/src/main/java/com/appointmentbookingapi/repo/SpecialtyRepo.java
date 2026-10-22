package com.appointmentbookingapi.repo;

import com.appointmentbookingapi.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyRepo extends JpaRepository<Specialty, Long> {

    Optional<Specialty> findByName(String name);

    boolean existsByName(String name);
}
