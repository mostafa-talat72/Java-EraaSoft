package com.tasklec11.service;

import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.dto.UserDTO;
import com.tasklec11.dto.UserSimpleDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserService {

    public UserSimpleDTO createUser(UserSimpleDTO userSimpleDTO);

    public UserSimpleDTO getUserById(Long id);

    @GetMapping
    public List<UserSimpleDTO> getAllUsers();

    public  UserSimpleDTO updateUser(Long id,UserSimpleDTO userSimpleDTO);

    public void deleteUser(Long id);

    public List<PostSimpleDTO> getPostsByParticularUserId(Long id);

    public List<UserDTO> getAllUsersWithPost();

    public UserDTO getUserWithPostById(Long id);
}
