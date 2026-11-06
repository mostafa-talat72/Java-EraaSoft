package com.restauranttablereservationapi.controller;

import com.restauranttablereservationapi.dto.RestaurantTableDTO;
import com.restauranttablereservationapi.dto.RestaurantTableRequestDTO;
import com.restauranttablereservationapi.service.RestaurantTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    @GetMapping
    public ResponseEntity<List<RestaurantTableDTO>> getAllTables() {
        return ResponseEntity.ok(restaurantTableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableDTO> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantTableService.getTableById(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantTableDTO> createTable(@Valid @RequestBody RestaurantTableRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantTableService.createTable(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTableDTO> updateTable(@PathVariable Long id,
                                                          @Valid @RequestBody RestaurantTableRequestDTO requestDTO) {
        return ResponseEntity.ok(restaurantTableService.updateTable(id, requestDTO));
    }

    // 204 on success; refused once the table has reservations.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        restaurantTableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
