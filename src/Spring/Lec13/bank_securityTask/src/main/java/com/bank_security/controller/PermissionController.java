package com.bank_security.controller;

import com.bank_security.dto.PermissionSimpleDTO;
import com.bank_security.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class PermissionController {

    private PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/admin/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionSimpleDTO> createPermission(@Valid @RequestBody PermissionSimpleDTO permissionSimpleDTO) {
        PermissionSimpleDTO createdPermission = permissionService.createPermission(permissionSimpleDTO);

        return ResponseEntity
                .created(URI.create("/admin/permissions/" + createdPermission.getId()))
                .body(createdPermission);
    }

    @GetMapping("/admin/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionSimpleDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/admin/permissions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionSimpleDTO> getPermissionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PutMapping("/admin/permissions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionSimpleDTO> updatePermission(@PathVariable("id") Long id, @Valid @RequestBody PermissionSimpleDTO permissionSimpleDTO) {
        return ResponseEntity.ok(permissionService.updatePermission(id, permissionSimpleDTO));
    }

    @DeleteMapping("/admin/permissions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id) {
        permissionService.deletePermissionById(id);
        return ResponseEntity.noContent().build();
    }
}
