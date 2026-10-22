package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.DoctorUnavailabilityDTO;
import com.appointmentbookingapi.dto.DoctorUnavailabilityRequestDTO;
import com.appointmentbookingapi.model.DoctorUnavailability;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorUnavailabilityService {

    List<DoctorUnavailabilityDTO> getAllDoctorUnavailabilities();

    // Rule 4 helper: the FIRST stored blocked range that overlaps the appointment
    // window [appointmentStart, appointmentEnd) for this doctor, or null if free.
    // "No overlap" is the happy path here, so null is returned instead of 404.
    DoctorUnavailability getDoctorUnavailabilityEntityByDoctorIdAndBeforeAndAfterTime(
            Long doctorId, LocalDateTime before, LocalDateTime after);

    DoctorUnavailabilityDTO getDoctorUnavailabilityById(Long id);

    DoctorUnavailabilityDTO createDoctorUnavailability(DoctorUnavailabilityRequestDTO requestDTO);

    void deleteDoctorUnavailability(Long id);
}
