package com.appointmentbookingapi.service.impl;

import com.appointmentbookingapi.dto.DoctorUnavailabilityDTO;
import com.appointmentbookingapi.dto.DoctorUnavailabilityRequestDTO;
import com.appointmentbookingapi.exception.BadRequestException;
import com.appointmentbookingapi.exception.NotFoundException;
import com.appointmentbookingapi.mapper.DoctorUnavailabilityMapper;
import com.appointmentbookingapi.model.Doctor;
import com.appointmentbookingapi.model.DoctorUnavailability;
import com.appointmentbookingapi.repo.DoctorUnavailabilityRepo;
import com.appointmentbookingapi.service.DoctorService;
import com.appointmentbookingapi.service.DoctorUnavailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorUnavailabilityServiceImpl implements DoctorUnavailabilityService {

    private final DoctorUnavailabilityRepo doctorUnavailabilityRepo;
    private final DoctorUnavailabilityMapper doctorUnavailabilityMapper;
    private final DoctorService doctorService;

    @Transactional(readOnly = true)
    @Override
    public List<DoctorUnavailabilityDTO> getAllDoctorUnavailabilities() {
        // Read-only SELECT of every blocked time range.
        List<DoctorUnavailability> doctorUnavailabilityList = doctorUnavailabilityRepo.findAll();

        // Empty table -> 404 with a meaningful message, never an empty string.
        if (doctorUnavailabilityList.isEmpty()) {
            throw new NotFoundException("No doctor unavailability found in the system");
        }

        return doctorUnavailabilityMapper
                .convertFromDoctorUnavailabilityListToDoctorUnavailabilityDtoList(doctorUnavailabilityList);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorUnavailability getDoctorUnavailabilityEntityByDoctorIdAndBeforeAndAfterTime(
            Long doctorId, LocalDateTime before, LocalDateTime after) {
        // Underlying query: doctor_id = ? AND start_date_time < :before AND end_date_time > :after
        // -> the standard overlap test when called with the REVERSED window args
        //    (appointmentEnd as "before", appointmentStart as "after").
        // Args must arrive in query order: (doctorId, beforeValue, afterValue).
        // findFirst keeps it cheap when several leaves overlap the booking.
        return doctorUnavailabilityRepo
                .findFirstByDoctorIdAndStartDateTimeBeforeAndEndDateTimeAfter(doctorId, before, after)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorUnavailabilityDTO getDoctorUnavailabilityById(Long id) {
        // 404 is thrown by the helper if the row does not exist.
        return doctorUnavailabilityMapper.convertFromDoctorUnavailabilityToDoctorUnavailabilityDto(
                findUnavailabilityOrThrow(id));
    }

    @Override
    public DoctorUnavailabilityDTO createDoctorUnavailability(DoctorUnavailabilityRequestDTO requestDTO) {

        // Defense-in-depth: the same checks exist as bean validation (@NotNull +
        // @AssertTrue), but the service stays safe even if called without @Valid
        // (e.g. from another service or a future scheduler).
        // end must be STRICTLY after start (equal start/end = zero-length = invalid).
        if (Objects.isNull(requestDTO.getStartDateTime())
                || Objects.isNull(requestDTO.getEndDateTime())
                || !requestDTO.getEndDateTime().isAfter(requestDTO.getStartDateTime())) {
            throw new BadRequestException(
                    "startDateTime and endDateTime are required, and endDateTime must be after startDateTime");
        }

        // Resolve the doctor (404 if the id does not exist) — the request carries
        // only doctorId, so the mapper leaves the relation null and we attach it here.
        Doctor doctor = doctorService.getDoctorEntityById(requestDTO.getDoctorId());


        // Field validation (@NotNull dates, cross-field start < end via @AssertTrue)
        // already ran in the model layer before reaching this point.

        DoctorUnavailability doctorUnavailability =
                doctorUnavailabilityMapper.convertFromDoctorUnavailabilityRequestDtoToDoctorUnavailability(requestDTO);
        doctorUnavailability.setDoctor(doctor);

        // Decision already agreed: adding unavailability does NOT touch existing
        // BOOKED appointments that overlap this range. Rule 4 (doctor must be
        // available) is enforced ONLY when new bookings are created in
        // AppointmentServiceImpl — past commitments stay as they are.
        return doctorUnavailabilityMapper.convertFromDoctorUnavailabilityToDoctorUnavailabilityDto(
                doctorUnavailabilityRepo.save(doctorUnavailability));
    }

    @Override
    public void deleteDoctorUnavailability(Long id) {
        // Fetch first so a wrong id results in 404 instead of a silent no-op DELETE.
        DoctorUnavailability doctorUnavailability = findUnavailabilityOrThrow(id);
        doctorUnavailabilityRepo.delete(doctorUnavailability);
    }

    // Shared lookup used by get and delete to keep messages consistent.
    private DoctorUnavailability findUnavailabilityOrThrow(Long id) {
        return doctorUnavailabilityRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Doctor unavailability with id " + id + " not found"));
    }
}
