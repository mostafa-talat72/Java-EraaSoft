package com.restauranttablereservationapi.service;

import com.restauranttablereservationapi.dto.ReservationStatusDTO;
import com.restauranttablereservationapi.dto.ReservationStatusRequestDTO;
import com.restauranttablereservationapi.model.ReservationStatus;
import jakarta.validation.Valid;

import java.util.List;

public interface ReservationStatusService {

    List<ReservationStatusDTO> getAllReservationStatuses();

    ReservationStatusDTO getReservationStatusById(Long id);

    // Raw entity for internal callers (e.g. ReservationServiceImpl).
    ReservationStatus getReservationStatusEntityById(Long id);

    // Lookup by the seeded name (PENDING / CONFIRMED / CANCELLED / EXPIRED).
    ReservationStatus getReservationStatusEntityByName(String name);

    ReservationStatusDTO createReservationStatus(@Valid ReservationStatusRequestDTO requestDTO);

    ReservationStatusDTO updateReservationStatus(Long id, @Valid ReservationStatusRequestDTO requestDTO);

    void deleteReservationStatus(Long id);
}
