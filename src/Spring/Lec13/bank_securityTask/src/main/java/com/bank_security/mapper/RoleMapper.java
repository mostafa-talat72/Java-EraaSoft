package com.bank_security.mapper;

import com.bank_security.dto.RoleDTO;
import com.bank_security.dto.RoleSimpleDTO;
import com.bank_security.model.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    public Role convertFromRoleDtoToRole(RoleDTO roleDTO);

    public RoleDTO convertFromRoleToRoleDto(Role role);

    public List<Role> convertFromRoleDtoListToRoleList(List<RoleDTO> roleDtoList);

    public List<RoleDTO> convertFromRoleListToRoleDtoList(List<Role> roles);

    public Role convertFromRoleSimpleDtoToRole(RoleSimpleDTO roleSimpleDTO);

    public RoleSimpleDTO convertFromRoleToRoleSimpleDto(Role role);

    public List<Role> convertFromRoleSimpleDtoListToRoleList(List<RoleSimpleDTO> roleSimpleDtoList);

    public List<RoleSimpleDTO> convertFromRoleListToRoleSimpleDtoList(List<Role> roles);

}
