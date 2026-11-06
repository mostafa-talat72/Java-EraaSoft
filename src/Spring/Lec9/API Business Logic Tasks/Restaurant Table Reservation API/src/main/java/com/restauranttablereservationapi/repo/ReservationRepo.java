package com.restauranttablereservationapi.repo;

import com.restauranttablereservationapi.model.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {


    // Referential guards used before deleting a table/customer/status.
    boolean existsByTableId(Long tableId);

    boolean existsByCustomerId(Long customerId);

    boolean existsByStatusId(Long statusId);

    // Reads: the three nested views are fetched in the same query (no N+1)
    // and every list has a stable order.
    @EntityGraph(attributePaths = {"status", "table", "customer"})
    List<Reservation> findAllByOrderByStartTimeAsc();

    @EntityGraph(attributePaths = {"status", "table", "customer"})
    List<Reservation> findByCustomerIdOrderByIdDesc(Long customerId);

    @EntityGraph(attributePaths = {"status", "table", "customer"})
    List<Reservation> findByTableIdOrderByStartTimeAsc(Long tableId);

    @EntityGraph(attributePaths = {"status", "table", "customer"})
    @Query("""
        select r from Reservation r
        where r.status.name = 'PENDING'
            and r.expiresAt < :now
     """)
    List<Reservation> findExpirePending(@Param("now") LocalDateTime date);

    @Query("""
        select count(r) from Reservation r
            where r.customer.id = :customerId
                and r.status.name in ('PENDING', 'CONFIRMED')
                    and :date < r.endTime
    """)
    Long countCustomerActiveReservation(@Param("customerId") Long customerId, @Param("date") LocalDateTime date);

    @Query("""
        select count(r) from Reservation r
            where r.table.id = :tableId
                and r.status.name in ('PENDING', 'CONFIRMED')
                    and :startTime < r.endTime
                        and :endTime > r.startTime
    """)
    Long countTableActiveReservation(@Param("tableId") Long tableId
            , @Param("startTime") LocalDateTime startTime,  @Param("endTime") LocalDateTime endTime);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reservation r where r.id = :id")
    Optional<Reservation> findByIdForUpdate(@Param("id") Long id);
}
