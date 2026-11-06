package com.restauranttablereservationapi.service.impl;

import com.restauranttablereservationapi.dto.RestaurantTableDTO;
import com.restauranttablereservationapi.dto.RestaurantTableRequestDTO;
import com.restauranttablereservationapi.exception.BadRequestException;
import com.restauranttablereservationapi.exception.NotFoundException;
import com.restauranttablereservationapi.helper.ReservationHelper;
import com.restauranttablereservationapi.mapper.RestaurantTableMapper;
import com.restauranttablereservationapi.model.RestaurantTable;
import com.restauranttablereservationapi.repo.RestaurantTableRepo;
import com.restauranttablereservationapi.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepo restaurantTableRepo;

    private final ReservationHelper reservationHelper;

    private final RestaurantTableMapper restaurantTableMapper;

    @Transactional(readOnly = true)
    @Override
    public List<RestaurantTableDTO> getAllTables() {
        List<RestaurantTable> tables = restaurantTableRepo.findAllByOrderByTableNumberAsc();

        // Same convention as the previous tasks: an empty list is a 404.
        if (tables.isEmpty()) {
            throw new NotFoundException("No tables found in the system");
        }

        return restaurantTableMapper.convertFromTableListToTableDtoList(tables);
    }

    @Override
    public RestaurantTable getTableEntityById(Long id) {
        return findTableOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public RestaurantTableDTO getTableById(Long id) {
        return restaurantTableMapper.convertFromTableToTableDto(findTableOrThrow(id));
    }

    @Override
    public RestaurantTableDTO createTable(RestaurantTableRequestDTO requestDTO) {
        // Pre-check the unique business key so a duplicate becomes a clear 400
        // instead of a raw DB unique-constraint 500.
        if (restaurantTableRepo.existsByTableNumber(requestDTO.getTableNumber())) {
            throw new BadRequestException(
                    "Table with number '" + requestDTO.getTableNumber() + "' already exists");
        }

        RestaurantTable table = restaurantTableMapper.convertFromTableRequestDtoToTable(requestDTO);

        // A missing "active" means "available", not "out of service".
        if (Objects.isNull(requestDTO.getActive())) {
            table.setActive(true);
        }

        return restaurantTableMapper.convertFromTableToTableDto(restaurantTableRepo.save(table));
    }

    @Override
    public RestaurantTableDTO updateTable(Long id, RestaurantTableRequestDTO requestDTO) {
        // Managed entity: dirty checking flushes the new values at commit.
        RestaurantTable table = findTableOrThrow(id);

        if (restaurantTableRepo.existsByTableNumberAndIdNot(requestDTO.getTableNumber(), id)) {
            throw new BadRequestException(
                    "Table with number '" + requestDTO.getTableNumber() + "' already exists");
        }

        restaurantTableMapper.updateTableFromTableRequestDto(requestDTO, table);

        return restaurantTableMapper.convertFromTableToTableDto(table);
    }

    @Override
    public void deleteTable(Long id) {
        RestaurantTable table = findTableOrThrow(id);

        // Refuse the delete while reservations still reference this table,
        // otherwise the FK constraint fails and the client gets a 500.
        if (reservationHelper.tableHasReservationHistory(id)) {
            throw new BadRequestException(
                    "Table with id " + id + " has reservations and cannot be deleted");
        }

        restaurantTableRepo.delete(table);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private RestaurantTable findTableOrThrow(Long id) {
        return restaurantTableRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Table with id " + id + " not found"));
    }
}
