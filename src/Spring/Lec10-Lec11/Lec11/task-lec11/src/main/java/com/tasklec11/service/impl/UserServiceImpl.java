package com.tasklec11.service.impl;

import com.tasklec11.dto.PostDTO;
import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.dto.UserDTO;
import com.tasklec11.dto.UserSimpleDTO;
import com.tasklec11.exception.PostException;
import com.tasklec11.exception.UserException;
import com.tasklec11.mapper.UserMapper;
import com.tasklec11.model.Post;
import com.tasklec11.model.User;
import com.tasklec11.repo.UserRepo;
import com.tasklec11.service.PostService;
import com.tasklec11.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private UserMapper userMapper;
    private PostService postService;
    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, PostService postService) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.postService = postService;
    }

    @Override
    public UserSimpleDTO createUser(UserSimpleDTO userSimpleDTO) {
       if(Objects.nonNull(userSimpleDTO.getId())){
           throw new UserException(
                   "id",
                   "User ID must be null when creating a new user"
           );
       }

       return userMapper.convertFromUserToUserSimpleDto(
               userRepo.save(
                       userMapper.convertFromUserSimpleDtoToUser(
                               userSimpleDTO
                       )
               )
       );
    }

    @Override
    public UserSimpleDTO getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new UserException(
                    "id",
                    "User does not exist with ID: " + id
            );
        }

        return userMapper.convertFromUserToUserSimpleDto(user.get());
    }

    @Override
    public List<UserSimpleDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        if(users.isEmpty()){
            throw new UserException(
                    "Users",
                    "No users found"
            );
        }

        return userMapper.convertFromUserListToUserSimpleDtoList(users);
    }

    @Override
    public UserSimpleDTO updateUser(Long id, UserSimpleDTO userSimpleDTO) {
        if(!userRepo.existsById(id)){
            throw new UserException(
                    "id",
                    "User does not exist with ID: " + id
            );
        }

        if(Objects.nonNull(userSimpleDTO.getId()) && !userSimpleDTO.getId().equals(id)){
            throw new UserException(
                    "id",
                    "User id in api link: " + id + " dose not equal user id in object you send: " + userSimpleDTO.getId()
            );
        }

        userSimpleDTO.setId(id);

        return userMapper.convertFromUserToUserSimpleDto(
                userRepo.save(
                        userMapper.convertFromUserSimpleDtoToUser(
                                userSimpleDTO
                        )
                )
        );
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepo.existsById(id)){
            throw new UserException(
                    "id",
                    "User does not exist with ID: " + id
            );
        }

        userRepo.deleteById(id);
    }

    @Override
    public List<PostSimpleDTO> getPostsByParticularUserId(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new UserException(
                    "id",
                    "User does not exist with ID: " + id
            );
        }

        return postService.getAllPostWithUser(user.get());
    }

    @Override
    public List<UserDTO> getAllUsersWithPost() {
        List<User> users = userRepo.findAll();
        if(users.isEmpty()){
            throw new UserException(
                    "Users",
                    "No users found"
            );
        }

        return userMapper.convertFromUserListToUserDtoList(users);
    }

    @Override
    public UserDTO getUserWithPostById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new UserException(
                    "id",
                    "User does not exist with ID: " + id
            );
        }

        return userMapper.convertFromUserToUserDto(user.get());
    }
}
