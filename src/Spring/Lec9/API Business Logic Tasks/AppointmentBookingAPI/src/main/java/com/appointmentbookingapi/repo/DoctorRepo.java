package com.appointmentbookingapi.repo;

import com.appointmentbookingapi.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {

    boolean existsByNameAndSpecialtyId(String name, Long specialtyId);
}
