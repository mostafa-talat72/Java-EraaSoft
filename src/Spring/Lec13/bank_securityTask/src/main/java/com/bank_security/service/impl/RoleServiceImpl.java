package com.bank_security.service.impl;

import com.bank_security.dto.RoleDTO;
import com.bank_security.dto.RoleSimpleDTO;
import com.bank_security.exception.CustomException;
import com.bank_security.mapper.RoleMapper;
import com.bank_security.model.Permission;
import com.bank_security.model.Role;
import com.bank_security.repo.RoleRepo;
import com.bank_security.service.PermissionService;
import com.bank_security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;

    private RoleMapper roleMapper;

    private PermissionService permissionService;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, RoleMapper roleMapper, PermissionService permissionService) {
        this.roleRepo = roleRepo;
        this.roleMapper = roleMapper;
        this.permissionService = permissionService;
    }

    @Override
    public Role findByName(String name) {
        return roleRepo.findByName(name).orElseThrow(
                () -> new CustomException(
                        "Role Not found Exception",
                        "Role not found with this name: " + name
                )
        );
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {

        if(Objects.nonNull(roleDTO.getId())){
            throw new CustomException(
                    "Id non null Exception",
                    "Id must be null to create role"
            );
        }

        if(roleRepo.existsByName(roleDTO.getName())){
            throw new CustomException(
                    "Duplicate Role Exception",
                    "Role already exists: " + roleDTO.getName()
            );
        }

        Role role = roleMapper.convertFromRoleDtoToRole(roleDTO);
        role.setId(null);
        role.setPermissions(resolvePermissions(roleDTO));

        Role savedRole = roleRepo.save(role);

        return roleMapper.convertFromRoleToRoleDto(savedRole);
    }

    @Override
    public List<RoleSimpleDTO> getAllRoles() {
        return roleMapper.convertFromRoleListToRoleSimpleDtoList(
                roleRepo.findAll()
        );
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        return roleMapper.convertFromRoleToRoleDto(getRoleEntity(id));
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {

        Role role = getRoleEntity(id);

        if(Objects.nonNull(roleDTO.getId()) && !roleDTO.getId().equals(id)){
            throw new CustomException(
                    "Id not equals Exception",
                    "Role id must be equal path variable id"
            );
        }

        if(Objects.nonNull(roleDTO.getName())){
            if(roleRepo.existsByNameAndIdNot(roleDTO.getName(), id)){
                throw new CustomException(
                        "Duplicate Role Exception",
                        "Role already exists: " + roleDTO.getName()
                );
            }
            role.setName(roleDTO.getName());
        }

        if(Objects.nonNull(roleDTO.getPermissions())){
            role.setPermissions(resolvePermissions(roleDTO));
        }

        Role savedRole = roleRepo.save(role);

        return roleMapper.convertFromRoleToRoleDto(savedRole);
    }

    @Override
    public void deleteRoleById(Long id) {

        Role role = getRoleEntity(id);

        if(Objects.nonNull(role.getCustomers()) && !role.getCustomers().isEmpty()){
            throw new CustomException(
                    "Role in use Exception",
                    "Role is assigned to customers and cannot be deleted: " + role.getName()
            );
        }

        roleRepo.delete(role);
    }


    private Role getRoleEntity(Long id) {
        return roleRepo.findById(id).orElseThrow(
                () -> new CustomException(
                        "Id Not found Exception",
                        "Role not found with this id: " + id
                )
        );
    }

    private List<Permission> resolvePermissions(RoleDTO roleDTO) {

        if(Objects.isNull(roleDTO.getPermissions())){
            return new ArrayList<>();
        }

        List<Permission> permissions = new ArrayList<>();

        roleDTO.getPermissions().forEach(permissionDto ->
                permissions.add(
                        permissionService.getPermissionEntityByName(permissionDto.getName())
                )
        );

        return permissions;
    }
}
