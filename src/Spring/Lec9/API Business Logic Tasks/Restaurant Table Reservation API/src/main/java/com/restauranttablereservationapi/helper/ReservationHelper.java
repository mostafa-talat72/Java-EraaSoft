package com.restauranttablereservationapi.helper;

import com.restauranttablereservationapi.exception.NotFoundException;
import com.restauranttablereservationapi.model.Customer;
import com.restauranttablereservationapi.model.Reservation;
import com.restauranttablereservationapi.model.ReservationStatus;
import com.restauranttablereservationapi.model.RestaurantTable;
import com.restauranttablereservationapi.repo.CustomerRepo;
import com.restauranttablereservationapi.repo.ReservationRepo;
import com.restauranttablereservationapi.repo.ReservationStatusRepo;
import com.restauranttablereservationapi.repo.RestaurantTableRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationHelper {

    private final CustomerRepo customerRepo;

    private final ReservationRepo reservationRepo;

    private final ReservationStatusRepo reservationStatusRepo;

    private final RestaurantTableRepo restaurantTableRepo;


    public boolean customerHasReservationHistory(Long customerId){

        return reservationRepo.existsByCustomerId(customerId);
    }

    public boolean statusHasReservationHistory(Long statusId){
        return reservationRepo.existsByStatusId(statusId);
    }

    public boolean tableHasReservationHistory(Long tableId){
        return reservationRepo.existsByTableId(tableId);
    }


    public Customer lockCustomerOrThrow(Long customerId){
        return customerRepo.findByIdForUpdate(customerId).orElseThrow(
                () -> new NotFoundException("Customer with id " + customerId + " not found")
        );
    }

    public RestaurantTable lockTableOrThrow(Long tableId){
        return restaurantTableRepo.findByIdForUpdate(tableId).orElseThrow(
                () -> new NotFoundException("Table with id " + tableId + " not found")
        );
    }

    public Reservation lockReservationOrThrow(Long reservationId){
        return reservationRepo.findByIdForUpdate(reservationId).orElseThrow(
                () -> new NotFoundException("Reservation with id " + reservationId + " not found")
        );
    }

    /**
     * Rule 7: flips PENDING rows whose 15-minute window already closed to EXPIRED.
     * Runs every minute in the background and at the start of every reservation
     * write and read, so no check ever sees a stale PENDING row.
     * @Transactional: on the scheduler path it opens its own transaction; when a
     * service calls it, it joins the caller's transaction and the managed rows
     * flush with it.
     */
    @Scheduled(cron = "0 * * * * *", zone = "Africa/Cairo")
    @Transactional
    public void refreshReservation(){
        List<Reservation> expireReservations = reservationRepo.findExpirePending(LocalDateTime.now());

        if(expireReservations.isEmpty())
            return;

        // one lookup for the whole batch
        ReservationStatus expiredStatus = reservationStatusRepo.findByName("EXPIRED").orElseThrow(
                () -> new NotFoundException("Reservation status 'EXPIRED' not found")
        );

        for(Reservation reservation : expireReservations){
            reservation.setStatus(expiredStatus);
        }
    }
}
