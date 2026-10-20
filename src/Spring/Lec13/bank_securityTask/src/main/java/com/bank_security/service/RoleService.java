package com.bank_security.service;

import com.bank_security.dto.RoleDTO;
import com.bank_security.dto.RoleSimpleDTO;
import com.bank_security.model.Role;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

// Service contract for Role.
// findByName stays unannotated: it runs inside the registration flow
// (role seeding for new customers) before any authority is established.
public interface RoleService {

    public Role findByName(String name);

    @PreAuthorize("hasRole('ADMIN')")
    public RoleDTO createRole(RoleDTO roleDTO);

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleSimpleDTO> getAllRoles();

    @PreAuthorize("hasRole('ADMIN')")
    public RoleDTO getRoleById(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    public RoleDTO updateRole(Long id, RoleDTO roleDTO);

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoleById(Long id);
}
