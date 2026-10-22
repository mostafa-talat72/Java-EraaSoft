package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.AppointmentStatusDTO;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.AppointmentStatusMapper;
import com.appointmentbookingapi.model.AppointmentStatus;
import com.appointmentbookingapi.repo.AppointmentStatusRepo;
import com.appointmentbookingapi.service.AppointmentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentStatusServiceImpl implements AppointmentStatusService {

    private final AppointmentStatusRepo appointmentStatusRepo;
    private final AppointmentStatusMapper appointmentStatusMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentStatusDTO> getAllAppointmentStatuses() {
        // Read-only SELECT of the status dictionary (BOOKED / CANCELED, seeded by the seeder).
        List<AppointmentStatus> statuses = appointmentStatusRepo.findAll();

        // Empty table -> 404 with a meaningful message, never an empty string.
        // (Should never happen because the seeder inserts both rows at startup.)
        if (statuses.isEmpty()) {
            throw new NotFoundException("No appointment statuses found in the system");
        }

        return appointmentStatusMapper.convertFromAppointmentStatusListToAppointmentStatusDtoList(statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentStatus getAppointmentStatusEntityById(Long id) {
        // Raw entity without mapping — used by AppointmentServiceImpl to attach
        // the status row when creating or cancelling an appointment.
        return findStatusOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentStatusDTO getAppointmentStatusById(Long id) {
        // 404 is thrown by the helper if the row does not exist.
        return appointmentStatusMapper.convertFromAppointmentStatusToAppointmentStatusDto(
                findStatusOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentStatus getAppointmentStatusEntityByName(String name) {
        // Dictionary lookup by @Pattern-constrained name (BOOKED | CANCELED).
        // Used by the booking flow to resolve the status row before insert:
        // rule 6 relies on switching BOOKED -> CANCELED on cancellation.
        return appointmentStatusRepo.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        "Appointment status with name '" + name + "' not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentStatusDTO getAppointmentStatusByName(String name) {
        // Same lookup as the entity variant, returned as a response DTO (no logic duplication).
        return appointmentStatusMapper.convertFromAppointmentStatusToAppointmentStatusDto(
                getAppointmentStatusEntityByName(name));
    }

    // Shared lookup used by getById to keep messages consistent.
    private AppointmentStatus findStatusOrThrow(Long id) {
        return appointmentStatusRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Appointment status with id " + id + " not found"));
    }
}
