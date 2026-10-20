package com.bank_security.service;

import com.bank_security.dto.PermissionSimpleDTO;
import com.bank_security.model.Permission;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

// Service contract for Permission.
// getPermissionEntityByName stays unannotated: it is internal plumbing,
// resolving permission names into managed entities for role creation/updates.
public interface PermissionService {

    public Permission getPermissionEntityByName(String name);

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionSimpleDTO createPermission(PermissionSimpleDTO permissionSimpleDTO);

    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionSimpleDTO> getAllPermissions();

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionSimpleDTO getPermissionById(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionSimpleDTO updatePermission(Long id, PermissionSimpleDTO permissionSimpleDTO);

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePermissionById(Long id);
}
