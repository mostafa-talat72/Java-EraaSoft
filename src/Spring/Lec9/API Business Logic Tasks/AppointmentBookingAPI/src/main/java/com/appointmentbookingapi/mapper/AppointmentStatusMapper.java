package com.appointmentbookingapi.mapper;

import com.appointmentbookingapi.dto.AppointmentStatusDTO;
import com.appointmentbookingapi.model.AppointmentStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentStatusMapper {

    AppointmentStatusDTO convertFromAppointmentStatusToAppointmentStatusDto(AppointmentStatus appointmentStatus);

    AppointmentStatus convertFromAppointmentStatusDtoToAppointmentStatus(AppointmentStatusDTO appointmentStatusDto);

    List<AppointmentStatus> convertFromAppointmentStatusDtoListToAppointmentStatusList(List<AppointmentStatusDTO> appointmentStatusDtoList);

    List<AppointmentStatusDTO> convertFromAppointmentStatusListToAppointmentStatusDtoList(List<AppointmentStatus> appointmentStatuses);
}
