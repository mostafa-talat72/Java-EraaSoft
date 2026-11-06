package com.restauranttablereservationapi.mapper;

import com.restauranttablereservationapi.dto.ReservationDTO;
import com.restauranttablereservationapi.model.Reservation;
import org.mapstruct.Mapper;

import java.util.List;

// The nested table/customer/status views are produced by the other mappers.
@Mapper(componentModel = "spring",
        uses = {RestaurantTableMapper.class, CustomerMapper.class, ReservationStatusMapper.class})
public interface ReservationMapper {

    ReservationDTO convertFromReservationToReservationDto(Reservation reservation);

    List<ReservationDTO> convertFromReservationListToReservationDtoList(List<Reservation> reservations);
}
