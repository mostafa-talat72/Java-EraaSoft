package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.DoctorDTO;
import com.appointmentbookingapi.dto.DoctorRequestDTO;
import com.appointmentbookingapi.model.Doctor;

import java.util.List;

public interface DoctorService {

    List<DoctorDTO> getAllDoctors();

    Doctor getDoctorEntityById(Long id);

    DoctorDTO getDoctorById(Long id);

    DoctorDTO createDoctor(DoctorRequestDTO requestDTO);

    void deleteDoctor(Long id);
}
