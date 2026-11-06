package com.restauranttablereservationapi.service.impl;

import com.restauranttablereservationapi.dto.ReservationStatusDTO;
import com.restauranttablereservationapi.dto.ReservationStatusRequestDTO;
import com.restauranttablereservationapi.exception.BadRequestException;
import com.restauranttablereservationapi.exception.NotFoundException;
import com.restauranttablereservationapi.helper.ReservationHelper;
import com.restauranttablereservationapi.mapper.ReservationStatusMapper;
import com.restauranttablereservationapi.model.ReservationStatus;
import com.restauranttablereservationapi.repo.ReservationStatusRepo;
import com.restauranttablereservationapi.service.ReservationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class ReservationStatusServiceImpl implements ReservationStatusService {

    private final ReservationStatusRepo reservationStatusRepo;

    private final ReservationHelper reservationHelper;

    private final ReservationStatusMapper reservationStatusMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ReservationStatusDTO> getAllReservationStatuses() {
        List<ReservationStatus> statuses = reservationStatusRepo.findAllByOrderByIdAsc();

        if (statuses.isEmpty()) {
            throw new NotFoundException("No reservation statuses found in the system");
        }

        return reservationStatusMapper.convertFromStatusListToStatusDtoList(statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public ReservationStatusDTO getReservationStatusById(Long id) {
        return reservationStatusMapper.convertFromStatusToStatusDto(findStatusOrThrow(id));
    }

    @Override
    public ReservationStatus getReservationStatusEntityById(Long id) {
        return findStatusOrThrow(id);
    }

    @Override
    public ReservationStatus getReservationStatusEntityByName(String name) {
        return reservationStatusRepo.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        "Reservation status with name '" + name + "' not found"));
    }

    @Override
    public ReservationStatusDTO createReservationStatus(ReservationStatusRequestDTO requestDTO) {
        if (reservationStatusRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException(
                    "Reservation status '" + requestDTO.getName() + "' already exists");
        }

        ReservationStatus status =
                reservationStatusMapper.convertFromStatusRequestDtoToStatus(requestDTO);

        return reservationStatusMapper.convertFromStatusToStatusDto(
                reservationStatusRepo.save(status));
    }

    @Override
    public ReservationStatusDTO updateReservationStatus(Long id,
                                                        ReservationStatusRequestDTO requestDTO) {
        ReservationStatus status = findStatusOrThrow(id);

        if (reservationStatusRepo.existsByNameAndIdNot(requestDTO.getName(), id)) {
            throw new BadRequestException(
                    "Reservation status '" + requestDTO.getName() + "' already exists");
        }

        reservationStatusMapper.updateStatusFromStatusRequestDto(requestDTO, status);

        return reservationStatusMapper.convertFromStatusToStatusDto(status);
    }

    @Override
    public void deleteReservationStatus(Long id) {
        ReservationStatus status = findStatusOrThrow(id);

        if (reservationHelper.statusHasReservationHistory(id)) {
            throw new BadRequestException(
                    "Reservation status with id " + id
                            + " is assigned to reservations and cannot be deleted");
        }

        reservationStatusRepo.delete(status);
    }

    private ReservationStatus findStatusOrThrow(Long id) {
        return reservationStatusRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Reservation status with id " + id + " not found"));
    }
}
