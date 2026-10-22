package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.AppointmentDTO;
import com.appointmentbookingapi.dto.AppointmentRequestDTO;
import com.appointmentbookingapi.exception.BadRequestException;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.AppointmentMapper;
import com.appointmentbookingapi.model.Appointment;
import com.appointmentbookingapi.model.AppointmentStatus;
import com.appointmentbookingapi.model.Doctor;
import com.appointmentbookingapi.model.DoctorUnavailability;
import com.appointmentbookingapi.model.MedicalService;
import com.appointmentbookingapi.model.Patient;
import com.appointmentbookingapi.repo.AppointmentRepo;
import com.appointmentbookingapi.service.AppointmentService;
import com.appointmentbookingapi.service.AppointmentStatusService;
import com.appointmentbookingapi.service.DoctorService;
import com.appointmentbookingapi.service.DoctorUnavailabilityService;
import com.appointmentbookingapi.service.MedicalServiceService;
import com.appointmentbookingapi.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    // Dictionary literals — statuses are seeded rows, not free text.
    private static final String STATUS_BOOKED = "BOOKED";
    private static final String STATUS_CANCELED = "CANCELED";

    // Rule 2 threshold: a patient cannot hold more than 2 BOOKED appointments per day.
    private static final int MAX_PATIENT_APPOINTMENTS_PER_DAY = 2;

    // Rule 5 threshold: cancellation accepted only until 2 hours before the start.
    private static final int CANCELLATION_CUTOFF_HOURS = 2;

    private final AppointmentRepo appointmentRepo;
    private final AppointmentMapper appointmentMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final MedicalServiceService medicalServiceService;
    private final DoctorUnavailabilityService doctorUnavailabilityService;
    private final AppointmentStatusService appointmentStatusService;

    @Override
    public AppointmentDTO createAppointment(AppointmentRequestDTO requestDTO) {
        // Field validation (ids @NotNull, date @Future) already ran via @Valid.

        // ─── Resolve every relation, 404 on the first missing id ───
        Doctor doctor = doctorService.getDoctorEntityById(requestDTO.getDoctorId());
        Patient patient = patientService.getPatientEntityById(requestDTO.getPatientId());
        MedicalService medicalService = medicalServiceService.getMedicalServiceEntityById(requestDTO.getServiceId());

        // ─── Rule 3: duration comes from the service type ───
        // The client sends only the START; the end is computed here and never stored
        // (single source of truth: start + service.durationMinutes).
        LocalDateTime appointmentStart = requestDTO.getAppointmentDateTime();
        LocalDateTime appointmentEnd = appointmentStart.plusMinutes(medicalService.getDurationMinutes());

        // ─── Rule 4: doctor must have no blocked range overlapping [start, end) ───
        // NOTE the reversed args (end first) — see the helper's javadoc.
        // A non-null result means conflict; we answer 400 with its exact leaf range.
        DoctorUnavailability conflict = doctorUnavailabilityService
                .getDoctorUnavailabilityEntityByDoctorIdAndBeforeAndAfterTime(
                        doctor.getId(), appointmentEnd, appointmentStart);

        if (Objects.nonNull(conflict)) {
            throw new BadRequestException("Doctor is unavailable from "
                    + conflict.getStartDateTime() + " to " + conflict.getEndDateTime());
        }

        // ─── Rule 1: doctor cannot have two overlapping BOOKED appointments ───
        // Fetch the doctor's BOOKED appointments of that day (candidate set),
        // then evaluate precise overlap in Java because the end is computed:
        // existingStart < newEnd && existingEnd > newStart.
        LocalDateTime dayStart = appointmentStart.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1).minusNanos(1);

        List<Appointment> doctorDayBooked = appointmentRepo
                .findByDoctorIdAndStatusNameAndAppointmentDateTimeBetween(
                        doctor.getId(), STATUS_BOOKED, dayStart, dayEnd);

        for (Appointment existing : doctorDayBooked) {
            LocalDateTime existingStart = existing.getAppointmentDateTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(
                    existing.getService().getDurationMinutes());

            if (existingStart.isBefore(appointmentEnd) && existingEnd.isAfter(appointmentStart)) {
                throw new BadRequestException("Doctor is already booked from "
                        + existingStart + " to " + existingEnd);
            }
        }

        // ─── Rule 2: patient cannot exceed 2 BOOKED appointments per day ───
        long patientBookedCount = appointmentRepo
                .countByPatientIdAndStatusNameAndAppointmentDateTimeBetween(
                        patient.getId(), STATUS_BOOKED, dayStart, dayEnd);

        if (patientBookedCount >= MAX_PATIENT_APPOINTMENTS_PER_DAY) {
            throw new BadRequestException("Patient already has "
                    + MAX_PATIENT_APPOINTMENTS_PER_DAY + " booked appointments on "
                    + appointmentStart.toLocalDate());
        }

        // ─── All rules passed: assemble and persist ───
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setService(medicalService);
        appointment.setAppointmentDateTime(appointmentStart);
        // Status row resolved from the dictionary (BOOKED).
        appointment.setStatus(
                appointmentStatusService.getAppointmentStatusEntityByName(STATUS_BOOKED));
        // createdAt is filled by the entity's @PrePersist.

        return appointmentMapper.convertFromAppointmentToAppointmentDto(
                appointmentRepo.save(appointment));
    }

    @Override
    public AppointmentDTO cancelAppointment(Long id) {
        Appointment appointment = findAppointmentOrThrow(id);

        // ─── Rule 6: a canceled appointment cannot be re-canceled ───
        if (STATUS_CANCELED.equals(appointment.getStatus().getName())) {
            throw new BadRequestException("Appointment is already canceled");
        }

        // ─── Rule 5: allowed only strictly more than 2 hours before the start ───
        // If now >= start - 2h (or the appointment already started), reject.
        if (!LocalDateTime.now().isBefore(
                appointment.getAppointmentDateTime().minusHours(CANCELLATION_CUTOFF_HOURS))) {
            throw new BadRequestException("Cancellation is allowed only more than "
                    + CANCELLATION_CUTOFF_HOURS + " hours before the appointment");
        }

        // Transition BOOKED -> CANCELED (the row stays; rule 6 guards re-booking the row).
        appointment.setStatus(
                appointmentStatusService.getAppointmentStatusEntityByName(STATUS_CANCELED));

        return appointmentMapper.convertFromAppointmentToAppointmentDto(
                appointmentRepo.save(appointment));
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentMapper.convertFromAppointmentToAppointmentDto(
                findAppointmentOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentDTO> getDoctorAppointments(Long doctorId, LocalDate date) {
        // 404 if the doctor id does not exist (keeps list endpoints honest).
        Doctor doctor = doctorService.getDoctorEntityById(doctorId);

        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1).minusNanos(1);

        // An empty result is a NORMAL answer here (the doctor is simply free that
        // day) -> returns [] instead of 404, unlike the getAll endpoints.
        List<Appointment> appointments = appointmentRepo
                .findByDoctorIdAndStatusNameAndAppointmentDateTimeBetween(
                        doctor.getId(), STATUS_BOOKED, dayStart, dayEnd);

        return appointmentMapper.convertFromAppointmentListToAppointmentDtoList(appointments);
    }

    // Shared lookup used by get and cancel to keep messages consistent.
    private Appointment findAppointmentOrThrow(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Appointment with id " + id + " not found"));
    }
}
