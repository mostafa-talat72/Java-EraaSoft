package com.restauranttablereservationapi.service.impl;

import com.restauranttablereservationapi.config.RestaurantProperties;
import com.restauranttablereservationapi.dto.ReservationDTO;
import com.restauranttablereservationapi.dto.ReservationRequestDTO;
import com.restauranttablereservationapi.exception.BadRequestException;
import com.restauranttablereservationapi.exception.NotFoundException;
import com.restauranttablereservationapi.helper.ReservationHelper;
import com.restauranttablereservationapi.mapper.ReservationMapper;
import com.restauranttablereservationapi.model.Customer;
import com.restauranttablereservationapi.model.Reservation;
import com.restauranttablereservationapi.model.ReservationStatus;
import com.restauranttablereservationapi.model.RestaurantTable;
import com.restauranttablereservationapi.repo.ReservationRepo;
import com.restauranttablereservationapi.service.CustomerService;
import com.restauranttablereservationapi.service.ReservationService;
import com.restauranttablereservationapi.service.ReservationStatusService;
import com.restauranttablereservationapi.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

// Rules 1..7 live here. "now" is read once per operation with LocalDateTime.now(),
// and every write path calls refreshReservation() first so the checks below see
// the real stored status (a PENDING row past its window is already EXPIRED).
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final ReservationMapper reservationMapper;
    private final ReservationHelper reservationHelper;
    // statuses are entities: resolve PENDING / CONFIRMED / CANCELLED / EXPIRED by name
    private final ReservationStatusService reservationStatusService;
    private final CustomerService customerService;
    private final RestaurantTableService restaurantTableService;

    private final RestaurantProperties restaurantProperties;


    @Override
    public ReservationDTO createReservation(ReservationRequestDTO requestDTO) {
        // Expire stale PENDING rows first: rule 4 must not count a reservation
        // whose 15-minute window already closed as still active.
        reservationHelper.refreshReservation();

        // Lock the customer row so two concurrent requests from the same customer
        // cannot both pass the active-reservation check below.
        Customer customer = reservationHelper.lockCustomerOrThrow(requestDTO.getCustomerId());

        // One "now" for the whole operation: rule 4, rule 5 and createdAt/expiresAt
        // must all be based on the same instant.
        LocalDateTime createdAt = LocalDateTime.now();

        // rule 4: at most maxActiveReservations rows that are PENDING/CONFIRMED
        // and have not ended yet.
        if(reservationRepo.countCustomerActiveReservation(requestDTO.getCustomerId(), createdAt) >= restaurantProperties.getMaxActiveReservations()){
            throw new BadRequestException("Customer with id " + requestDTO.getCustomerId()
                    + " already has " + restaurantProperties.getMaxActiveReservations()
                    + " active reservations");
        }

        // Lock the table row so the overlap check below cannot run in parallel
        // for the same table.
        RestaurantTable restaurantTable = reservationHelper.lockTableOrThrow(requestDTO.getTableId());

        // a table that is out of service accepts no new reservations
        if(!restaurantTable.isActive()){
            throw new BadRequestException("Table " + restaurantTable.getId()
                    + " is out of service and cannot be reserved");
        }

        LocalDateTime startTime = requestDTO.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(requestDTO.getDurationMinutes());

        // rule 1: half-open intervals [start, end) overlap when
        // existing.start < new.end && new.start < existing.end,
        // so back-to-back reservations are allowed.
        if(reservationRepo.countTableActiveReservation(requestDTO.getTableId(), startTime, endTime) > 0){
            throw new BadRequestException("Table " + requestDTO.getTableId()
                    + " is already reserved between " + startTime + " and " + endTime);
        }

        // rule 2: the party must fit the table capacity.
        if(requestDTO.getGuests() > restaurantTable.getCapacity()){
            throw new BadRequestException("Table " + restaurantTable.getId() + " seats "
                    + restaurantTable.getCapacity() + " guests, but the request is for "
                    + requestDTO.getGuests());
        }

        // rule 3: the working-hours window belongs to the start day. Comparing end
        // with the same-day closing also rejects any reservation crossing midnight.
        LocalDateTime opening = startTime.toLocalDate().atTime(restaurantProperties.getOpenTime());
        LocalDateTime closing = startTime.toLocalDate().atTime(restaurantProperties.getCloseTime());
        if(startTime.isBefore(opening) || endTime.isAfter(closing)){
            throw new BadRequestException("Reservations are only allowed between "
                    + restaurantProperties.getOpenTime() + " and "
                    + restaurantProperties.getCloseTime());
        }

        // rule 5: the start must be at least minAdvanceMinutes ahead of now.
        if(startTime.isBefore(createdAt.plusMinutes(restaurantProperties.getMinAdvanceMinutes()))){
            throw new BadRequestException("Reservations must be made at least "
                    + restaurantProperties.getMinAdvanceMinutes() + " minutes in advance");
        }

        // rule 7: the client has confirmWindowMinutes from creation to confirm.
        LocalDateTime expireAt = createdAt.plusMinutes(restaurantProperties.getConfirmWindowMinutes());

        ReservationStatus reservationStatus = reservationStatusService.getReservationStatusEntityByName("PENDING");

        // New rows always start as PENDING; confirmedAt/cancelledAt stay null until
        // the matching transition happens.
        Reservation reservation = new Reservation();

        reservation.setStatus(reservationStatus);
        reservation.setCustomer(customer);
        reservation.setTable(restaurantTable);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setGuests(requestDTO.getGuests());
        reservation.setCreatedAt(createdAt);
        reservation.setExpiresAt(expireAt);

        return reservationMapper.convertFromReservationToReservationDto(
                reservationRepo.save(reservation)
        );
    }

    @Override
    public ReservationDTO confirmReservation(Long id) {

        // flip PENDING rows whose window already closed to EXPIRED
        reservationHelper.refreshReservation();

        // Row lock: only one confirm can win the PENDING -> CONFIRMED transition.
        Reservation reservation = reservationHelper.lockReservationOrThrow(id);
        LocalDateTime now = LocalDateTime.now();

        // only a pending reservation can be confirmed
        if(!"PENDING".equals(reservation.getStatus().getName())){
            throw new BadRequestException("Reservation " + id + " is "
                    + reservation.getStatus().getName() + " and cannot be confirmed");
        }

        // covers the race where expiresAt passed between refreshReservation() and
        // acquiring the row lock
        if (!reservation.getExpiresAt().isAfter(now)) {
            throw new BadRequestException("Reservation " + id
                    + " expired at " + reservation.getExpiresAt()
                    + " and can no longer be confirmed");
        }

        ReservationStatus reservationStatus = reservationStatusService.getReservationStatusEntityByName("CONFIRMED");

        // PENDING -> CONFIRMED, and record when the confirmation happened
        reservation.setStatus(reservationStatus);
        reservation.setConfirmedAt(now);

        return reservationMapper.convertFromReservationToReservationDto(
                reservationRepo.save(reservation)
        );
    }

    @Override
    public ReservationDTO cancelReservation(Long id) {
        reservationHelper.refreshReservation();

        // Row lock: serializes cancel with confirm on the same reservation.
        Reservation reservation = reservationHelper.lockReservationOrThrow(id);
        LocalDateTime now = LocalDateTime.now();

        String statusName = reservation.getStatus().getName();

        // only a live reservation can be cancelled; a PENDING row past its
        // confirm window was already flipped to EXPIRED by the refresh above
        if (!"PENDING".equals(statusName) && !"CONFIRMED".equals(statusName)) {
            throw new BadRequestException("Reservation " + id + " is "
                    + statusName + " and cannot be cancelled");
        }

        // rule 6: cancellation is allowed only before the cutoff window
        LocalDateTime cutoff = reservation.getStartTime()
                .minusMinutes(restaurantProperties.getCancelCutoffMinutes());

        if (!now.isBefore(cutoff)) {
            throw new BadRequestException("Reservation " + id + " starts at "
                    + reservation.getStartTime() + " and cannot be cancelled within "
                    + restaurantProperties.getCancelCutoffMinutes()
                    + " minutes of its start time");
        }

        ReservationStatus reservationStatus =
                reservationStatusService.getReservationStatusEntityByName("CANCELLED");

        reservation.setStatus(reservationStatus);
        // cancelledAt records the cancellation; confirmedAt belongs to confirm only.
        reservation.setCancelledAt(now);

        return reservationMapper.convertFromReservationToReservationDto(
                reservationRepo.save(reservation)
        );
    }

    @Override
    public List<ReservationDTO> getAllReservations() {

        // refresh first so stale PENDING rows are returned as EXPIRED
        reservationHelper.refreshReservation();

        List<Reservation> reservations = reservationRepo.findAllByOrderByStartTimeAsc();

        if(reservations.isEmpty()){
            throw new NotFoundException("No reservations found in the system");
        }

        return reservationMapper.convertFromReservationListToReservationDtoList(reservations);
    }

    @Override
    public ReservationDTO getReservationById(Long id) {
        reservationHelper.refreshReservation();

        // missing id -> 404 through the shared helper
        return reservationMapper.convertFromReservationToReservationDto(findReservationOrThrow(id));
    }

    @Override
    public List<ReservationDTO> getReservationsByCustomer(Long customerId) {
        reservationHelper.refreshReservation();

        // 404 when the customer does not exist, then 404 when the history is empty
        Customer customer = customerService.getCustomerEntityById(customerId);
        List<Reservation> reservations = reservationRepo.findByCustomerIdOrderByIdDesc(customerId);

        if(reservations.isEmpty()){
            throw new NotFoundException("Customer with id " + customerId + " has no reservations");
        }

        return reservationMapper.convertFromReservationListToReservationDtoList(reservations);
    }

    @Override
    public List<ReservationDTO> getReservationsByTable(Long tableId) {
        reservationHelper.refreshReservation();

        // 404 when the table does not exist, then 404 when it has no reservations
        RestaurantTable restaurantTable = restaurantTableService.getTableEntityById(tableId);
        List<Reservation> reservations = reservationRepo.findByTableIdOrderByStartTimeAsc(tableId);

        if(reservations.isEmpty()){
            throw new NotFoundException("Table with id " + tableId + " has no reservations");
        }

        return reservationMapper.convertFromReservationListToReservationDtoList(reservations);
    }


    // Internal 404 helper shared by the single-reservation reads.
    private Reservation findReservationOrThrow(Long id) {
        return reservationRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Reservation with id " + id + " not found")
        );
    }
}
