package com.tasklec11.mapper;

import com.tasklec11.dto.UserDTO;
import com.tasklec11.dto.UserSimpleDTO;
import com.tasklec11.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public User convertFromUserDtoToUser(UserDTO userDTO);

    public UserDTO convertFromUserToUserDto(User user);

    public List<User> convertFromUserDtoListToUserList(List<UserDTO> userDTOList);

    public List<UserDTO> convertFromUserListToUserDtoList(List<User> users);

    public User convertFromUserSimpleDtoToUser(UserSimpleDTO userSimpleDTO);

    public UserSimpleDTO convertFromUserToUserSimpleDto(User user);

    public List<User> convertFromUserSimpleDtoListToUserList(List<UserSimpleDTO> userSimpleDTOList);

    public List<UserSimpleDTO> convertFromUserListToUserSimpleDtoList(List<User> users);


}
