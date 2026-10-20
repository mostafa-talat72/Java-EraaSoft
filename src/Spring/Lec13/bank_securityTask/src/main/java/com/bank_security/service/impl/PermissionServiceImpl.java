package com.bank_security.service.impl;

import com.bank_security.dto.PermissionSimpleDTO;
import com.bank_security.exception.CustomException;
import com.bank_security.mapper.PermissionMapper;
import com.bank_security.model.Permission;
import com.bank_security.repo.PermissionRepo;
import com.bank_security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PermissionServiceImpl implements PermissionService {

    private PermissionRepo permissionRepo;

    private PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionRepo permissionRepo, PermissionMapper permissionMapper) {
        this.permissionRepo = permissionRepo;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public Permission getPermissionEntityByName(String name) {
        return permissionRepo.findByName(name).orElseThrow(
                () -> new CustomException(
                        "Permission Not found Exception",
                        "Permission not found with this name: " + name
                )
        );
    }

    @Override
    public PermissionSimpleDTO createPermission(PermissionSimpleDTO permissionSimpleDTO) {

        if(Objects.nonNull(permissionSimpleDTO.getId())){
            throw new CustomException(
                    "Id non null Exception",
                    "Id must be null to create permission"
            );
        }

        if(permissionRepo.existsByName(permissionSimpleDTO.getName())){
            throw new CustomException(
                    "Duplicate Permission Exception",
                    "Permission already exists: " + permissionSimpleDTO.getName()
            );
        }

        Permission permission = permissionMapper.convertFromPermissionSimpleDtoToPermission(permissionSimpleDTO);
        permission.setId(null);

        Permission savedPermission = permissionRepo.save(permission);

        return permissionMapper.convertFromPermissionToPermissionSimpleDto(savedPermission);
    }

    @Override
    public List<PermissionSimpleDTO> getAllPermissions() {
        return permissionMapper.convertFromPermissionListToPermissionSimpleDtoList(
                permissionRepo.findAll()
        );
    }

    @Override
    public PermissionSimpleDTO getPermissionById(Long id) {
        return permissionMapper.convertFromPermissionToPermissionSimpleDto(
                getPermissionEntity(id)
        );
    }

    @Override
    public PermissionSimpleDTO updatePermission(Long id, PermissionSimpleDTO permissionSimpleDTO) {

        Permission permission = getPermissionEntity(id);

        if(Objects.nonNull(permissionSimpleDTO.getId()) && !permissionSimpleDTO.getId().equals(id)){
            throw new CustomException(
                    "Id not equals Exception",
                    "Permission id must be equal path variable id"
            );
        }

        if(Objects.nonNull(permissionSimpleDTO.getName())){
            if(permissionRepo.existsByNameAndIdNot(permissionSimpleDTO.getName(), id)){
                throw new CustomException(
                        "Duplicate Permission Exception",
                        "Permission already exists: " + permissionSimpleDTO.getName()
                );
            }
            permission.setName(permissionSimpleDTO.getName());
        }

        Permission savedPermission = permissionRepo.save(permission);

        return permissionMapper.convertFromPermissionToPermissionSimpleDto(savedPermission);
    }

    @Override
    public void deletePermissionById(Long id) {

        Permission permission = getPermissionEntity(id);

        if(Objects.nonNull(permission.getRoles()) && !permission.getRoles().isEmpty()){
            throw new CustomException(
                    "Permission in use Exception",
                    "Permission is attached to roles and cannot be deleted: " + permission.getName()
            );
        }

        permissionRepo.delete(permission);
    }


    private Permission getPermissionEntity(Long id) {
        return permissionRepo.findById(id).orElseThrow(
                () -> new CustomException(
                        "Id Not found Exception",
                        "Permission not found with this id: " + id
                )
        );
    }
}
