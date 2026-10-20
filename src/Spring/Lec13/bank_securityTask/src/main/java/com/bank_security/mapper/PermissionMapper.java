package com.bank_security.mapper;

import com.bank_security.dto.PermissionDTO;
import com.bank_security.dto.PermissionSimpleDTO;
import com.bank_security.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    public Permission convertFromPermissionDtoToPermission(PermissionDTO permissionDTO);

    public PermissionDTO convertFromPermissionToPermissionDto(Permission permission);

    public List<Permission> convertFromPermissionDtoListToPermissionList(List<PermissionDTO> permissionDtoList);

    public List<PermissionDTO> convertFromPermissionListToPermissionDtoList(List<Permission> permissions);

    public Permission convertFromPermissionSimpleDtoToPermission(PermissionSimpleDTO permissionSimpleDTO);

    public PermissionSimpleDTO convertFromPermissionToPermissionSimpleDto(Permission permission);

    public List<Permission> convertFromPermissionSimpleDtoListToPermissionList(List<PermissionSimpleDTO> permissionSimpleDtoList);

    public List<PermissionSimpleDTO> convertFromPermissionListToPermissionSimpleDtoList(List<Permission> permissions);

}
