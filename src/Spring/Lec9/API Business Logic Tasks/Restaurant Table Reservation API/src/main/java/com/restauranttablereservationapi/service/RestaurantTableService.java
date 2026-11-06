package com.restauranttablereservationapi.service;

import com.restauranttablereservationapi.dto.RestaurantTableDTO;
import com.restauranttablereservationapi.dto.RestaurantTableRequestDTO;
import com.restauranttablereservationapi.model.RestaurantTable;
import jakarta.validation.Valid;

import java.util.List;

public interface RestaurantTableService {

    RestaurantTableDTO createTable(@Valid RestaurantTableRequestDTO requestDTO);

    RestaurantTableDTO updateTable(Long id, @Valid RestaurantTableRequestDTO requestDTO);

    List<RestaurantTableDTO> getAllTables();

    // Raw entity for internal callers (e.g. ReservationServiceImpl).
    RestaurantTable getTableEntityById(Long id);

    RestaurantTableDTO getTableById(Long id);

    void deleteTable(Long id);
}
