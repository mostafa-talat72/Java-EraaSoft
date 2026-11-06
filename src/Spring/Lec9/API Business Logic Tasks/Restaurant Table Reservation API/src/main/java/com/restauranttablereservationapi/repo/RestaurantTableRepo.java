package com.restauranttablereservationapi.repo;

import com.restauranttablereservationapi.model.RestaurantTable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepo extends JpaRepository<RestaurantTable, Long> {

    // Locks the table row before the overlap check, so two concurrent
    // reservations for the same table are serialized (rule 1).
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from RestaurantTable t where t.id = :id")
    Optional<RestaurantTable> findByIdForUpdate(@Param("id") Long id);

    List<RestaurantTable> findAllByOrderByTableNumberAsc();

    boolean existsByTableNumber(String tableNumber);

    boolean existsByTableNumberAndIdNot(String tableNumber, Long id);
}
