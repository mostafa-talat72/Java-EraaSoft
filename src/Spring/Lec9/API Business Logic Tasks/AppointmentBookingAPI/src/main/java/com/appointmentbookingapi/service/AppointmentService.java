package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.AppointmentDTO;
import com.appointmentbookingapi.dto.AppointmentRequestDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentDTO createAppointment(AppointmentRequestDTO requestDTO);

    AppointmentDTO cancelAppointment(Long id);

    AppointmentDTO getAppointmentById(Long id);

    List<AppointmentDTO> getDoctorAppointments(Long doctorId, LocalDate date);
}
