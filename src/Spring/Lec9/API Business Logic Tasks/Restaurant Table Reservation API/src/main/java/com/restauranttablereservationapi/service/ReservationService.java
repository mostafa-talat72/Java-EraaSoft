package com.restauranttablereservationapi.service;

import com.restauranttablereservationapi.dto.ReservationDTO;
import com.restauranttablereservationapi.dto.ReservationRequestDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ReservationService {

    // POST /reservations - rules 1..5 and the PENDING / expiresAt state.
    ReservationDTO createReservation(@Valid ReservationRequestDTO requestDTO);

    // PUT /reservations/{id}/confirm - rule 7 (only inside the 15-minute window).
    ReservationDTO confirmReservation(Long id);

    // PUT /reservations/{id}/cancel - rule 6 (at least 1 hour before the start).
    ReservationDTO cancelReservation(Long id);

    // GET /reservations - every reservation, ordered by start time.
    List<ReservationDTO> getAllReservations();

    // GET /reservations/{id}
    ReservationDTO getReservationById(Long id);

    // GET /reservations/customer/{customerId}
    List<ReservationDTO> getReservationsByCustomer(Long customerId);

    // GET /reservations/table/{tableId}
    List<ReservationDTO> getReservationsByTable(Long tableId);


}
