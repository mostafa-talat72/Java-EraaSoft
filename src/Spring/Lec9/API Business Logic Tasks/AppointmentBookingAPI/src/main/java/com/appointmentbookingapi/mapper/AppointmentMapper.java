package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.AppointmentDTO;
import com.appointmentbookingapi.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        DoctorMapper.class,
        PatientMapper.class,
        MedicalServiceMapper.class,
        AppointmentStatusMapper.class
})
public interface AppointmentMapper {

    // endTime is rule 3 materialized: start + service.durationMinutes,
    // computed at mapping time and never persisted.
    @Mapping(
            target = "endTime",
            expression = "java(appointment.getAppointmentDateTime() != null && appointment.getService() != null && appointment.getService().getDurationMinutes() != null"
                    + " ? appointment.getAppointmentDateTime().plusMinutes(appointment.getService().getDurationMinutes()) : null)"
    )
    AppointmentDTO convertFromAppointmentToAppointmentDto(Appointment appointment);

    List<AppointmentDTO> convertFromAppointmentListToAppointmentDtoList(List<Appointment> appointments);
}
