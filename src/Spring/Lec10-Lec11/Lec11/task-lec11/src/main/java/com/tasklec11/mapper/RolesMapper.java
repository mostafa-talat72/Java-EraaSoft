package com.tasklec11.mapper;

import com.tasklec11.dto.RolesDTO;
import com.tasklec11.model.Roles;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolesMapper {

    public Roles convertFromRolesDtoToRoles(RolesDTO roleDTO);

    public RolesDTO convertFromRolesToRolesDto(Roles role);

    public List<Roles> convertFromRolesDtoListToRolesList(List<RolesDTO> roleDTOList);

    public List<RolesDTO> convertFromRolesListToRolesDtoList(List<Roles> roles);
    
}
