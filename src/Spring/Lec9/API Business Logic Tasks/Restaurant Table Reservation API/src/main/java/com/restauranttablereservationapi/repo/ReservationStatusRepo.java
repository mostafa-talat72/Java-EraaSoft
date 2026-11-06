package com.restauranttablereservationapi.repo;

import com.restauranttablereservationapi.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationStatusRepo extends JpaRepository<ReservationStatus, Long> {

    // The service resolves PENDING / CONFIRMED / CANCELLED / EXPIRED by name.
    Optional<ReservationStatus> findByName(String name);

    List<ReservationStatus> findAllByOrderByIdAsc();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
