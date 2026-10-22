package com.appointmentbookingapi.service;

import com.appointmentbookingapi.dto.AppointmentStatusDTO;
import com.appointmentbookingapi.model.AppointmentStatus;

import java.util.List;

public interface AppointmentStatusService {

    List<AppointmentStatusDTO> getAllAppointmentStatuses();

    AppointmentStatus getAppointmentStatusEntityById(Long id);

    AppointmentStatusDTO getAppointmentStatusById(Long id);

    AppointmentStatus getAppointmentStatusEntityByName(String name);

    AppointmentStatusDTO getAppointmentStatusByName(String name);
}
