package com.appointmentbookingapi.repo;

import com.appointmentbookingapi.model.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalServiceRepo extends JpaRepository<MedicalService, Long> {

    Optional<MedicalService> findByName(String name);

    boolean existsByName(String name);
}
